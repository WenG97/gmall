package com.weng.gulimall.search.service.impl;

import com.google.common.collect.Lists;
import com.weng.gulimall.model.list.SearchAttr;
import com.weng.gulimall.model.vo.search.*;

import com.weng.gulimall.common.constant.SysEsConst;
import com.weng.gulimall.model.list.Goods;
import com.weng.gulimall.search.repository.GoodsRepository;
import com.weng.gulimall.search.service.GoodsService;
import org.apache.lucene.search.join.ScoreMode;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.ParsedAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Parsed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;


    @Override
    public void savaGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }


    /**
     * ?????????ES????????????
     *
     * @param searchParamVo
     * @return
     */
    @Override
    public SearchResponseVo search(SearchParamVo searchParamVo) {
        //1?????????Query ????????????
        Query query = buildQueryDsl(searchParamVo);
        //2??????????????????es?????????
        SearchHits<Goods> goods = esRestTemplate.search(query,
                Goods.class,
                IndexCoordinates.of("goods"));

        //3?????????????????????????????????????????????SearchResponseVo
        return buildSearchResponseResult(goods, searchParamVo);
    }

    @Override
    public void updateHotscore(Long skuId, Long score) {
        Map<String,Long> map = new HashMap<>(1);
        map.put(SysEsConst.ORDER_FIELD_HOTSCORE,score);
        UpdateQuery updateQuery = UpdateQuery.builder(skuId.toString())
                .withDocument(Document.from(map))
                .build();

        esRestTemplate.update(updateQuery,IndexCoordinates.of(SysEsConst.INDEX_GOODS));
    }


    /**
     * ?????????????????????????????????????????????
     *
     * @param goods
     * @return
     */
    private SearchResponseVo buildSearchResponseResult(
            SearchHits<Goods> goods,
            SearchParamVo paramVo) {
        SearchResponseVo searchResponseVo = new SearchResponseVo();
        //1??????????????????????????????
        searchResponseVo.setSearchParam(paramVo);
        //2??????????????????????????????????????????????????????
        if (!StringUtils.isEmpty(paramVo.getTrademark())) {
            searchResponseVo.setTrademarkParam("??????" + paramVo.getTrademark().split(":")[1]);
        }
        //3????????????????????????
        if (!ObjectUtils.isEmpty(paramVo.getProps())) {
            List<SearchAttr> propsParamList = new ArrayList<>();
            Arrays.stream(paramVo.getProps()).forEach(prop -> {
                String[] split = prop.split(":");
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(Long.parseLong(split[0]));
                searchAttr.setAttrValue(split[1]);
                searchAttr.setAttrName(split[2]);
                propsParamList.add(searchAttr);
            });
            searchResponseVo.setPropsParamList(propsParamList);
        }
        //4????????????????????????????????????????????????????????????
        List<TrademarkVo> trademarkVos = buildTrademarkList(goods);
        searchResponseVo.setTrademarkList(trademarkVos);
        //5????????????????????????????????????????????????????????????
        List<AttrVo> attrVos = buildAttrList(goods);
        searchResponseVo.setAttrsList(attrVos);
        //6?????????????????????
        if (!ObjectUtils.isEmpty(paramVo.getOrder())) {
            String[] split = paramVo.getOrder().split(":");
            OrderMapVo orderMapVo = new OrderMapVo();
            orderMapVo.setSort(split[1]);
            orderMapVo.setType(split[0]);
            searchResponseVo.setOrderMap(orderMapVo);
        }
        //7????????????????????????????????????
        ArrayList<Goods> goodsList = new ArrayList<>();
        if (goods.getTotalHits() > 0) {
            List<SearchHit<Goods>> searchHits = goods.getSearchHits();
            searchHits.forEach(hit -> {
                Goods content = hit.getContent();
                //????????????????????????????????????????????????????????????????????????
                if (!StringUtils.isEmpty(paramVo.getKeyword())) {
                    String highLight = hit.getHighlightField(SysEsConst.QUERY_TITLE).get(0);
                    content.setTitle(highLight);
                }
                goodsList.add(content);
            });
        }
        searchResponseVo.setGoods(goodsList);
        //8?????????
        searchResponseVo.setPageNo(paramVo.getPageNo());
        //9????????????
        long totalPages = goods.getTotalHits() % SysEsConst.SEARCH_PAGE_SIZE == 0 ?
                goods.getTotalHits() / SysEsConst.SEARCH_PAGE_SIZE :
                goods.getTotalHits() / SysEsConst.SEARCH_PAGE_SIZE + 1;
        searchResponseVo.setTotalPages(Integer.parseInt(totalPages + ""));
        //10??????????????????
        String url = makeUrlParam(paramVo);
        searchResponseVo.setUrlParam(url);
        return searchResponseVo;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param goods
     * @return
     */
    private List<AttrVo> buildAttrList(SearchHits<Goods> goods) {
        List<AttrVo> attrVos = new ArrayList<>();

        ParsedNested attrAgg = goods.getAggregations().get(SysEsConst.NESTED_AGG_ATTR_AGG);
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get(SysEsConst.AGG_ATTR_ID_AGG);
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            AttrVo attrVo = new AttrVo();


            long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);

            ParsedTerms attrNameAgg = bucket.getAggregations().get(SysEsConst.AGG_ATTR_NAME_AGG);
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);

            ParsedTerms attrValueAgg = bucket.getAggregations().get(SysEsConst.AGG_ATTR_VALUE_AGG);
            List<String> attrValues = new ArrayList<>();
            for (Terms.Bucket attrValueAggBucket : attrValueAgg.getBuckets()) {
                String attrValue = attrValueAggBucket.getKeyAsString();
                attrValues.add(attrValue);
            }
            attrVo.setAttrValueList(attrValues);
            attrVos.add(attrVo);
        }
        return attrVos;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param goods
     * @return
     */
    private List<TrademarkVo> buildTrademarkList(SearchHits<Goods> goods) {
        List<TrademarkVo> trademarkVos = new ArrayList<>();
        Aggregations aggregations = goods.getAggregations();
        ParsedTerms tmIdAgg = aggregations.get(SysEsConst.AGG_TMID_AGG);
        for (Terms.Bucket bucket : tmIdAgg.getBuckets()) {
            TrademarkVo trademarkVo = new TrademarkVo();

            Long tmId = bucket.getKeyAsNumber().longValue();
            trademarkVo.setTmId(tmId);
            //???????????????
            ParsedTerms tmNameAgg = bucket.getAggregations().get(SysEsConst.AGG_TM_NAME_AGG);
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmName(tmName);
            //????????????logo
            ParsedTerms tmLogoAgg = bucket.getAggregations().get(SysEsConst.AGG_TM_LOGO_AGG);
            String tmLogo = tmLogoAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmLogoUrl(tmLogo);
            trademarkVos.add(trademarkVo);
        }
        return trademarkVos;
    }


    /**
     * ???????????????
     *
     * @param paramVo
     * @return
     */
    private String makeUrlParam(SearchParamVo paramVo) {
        StringBuilder stringBuilder = new StringBuilder("list.html?");
        if (!ObjectUtils.isEmpty(paramVo.getCategory1Id())) {
            stringBuilder.append("&category1Id=" + paramVo.getCategory1Id());
        }
        if (!ObjectUtils.isEmpty(paramVo.getCategory2Id())) {
            stringBuilder.append("&category2Id=" + paramVo.getCategory2Id());
        }
        if (!ObjectUtils.isEmpty(paramVo.getCategory3Id())) {
            stringBuilder.append("&category3Id=" + paramVo.getCategory3Id());
        }

        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            stringBuilder.append("&keyword=" + paramVo.getKeyword());
        }
        if (!StringUtils.isEmpty(paramVo.getTrademark())) {
            stringBuilder.append("&trademark=" + paramVo.getTrademark());
        }

        if (!ObjectUtils.isEmpty(paramVo.getProps())) {
            Arrays.stream(paramVo.getProps()).forEach(o -> {
                stringBuilder.append("&props=" + o);
            });
        }
        // stringBuilder.append("&order=" + paramVo.getOrder());
        // stringBuilder.append("&pageNo" + paramVo.getPageNo());
        return stringBuilder.toString();
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param paramVo
     * @return
     */
    private Query buildQueryDsl(SearchParamVo paramVo) {
        //1????????????????????????????????????
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //2???????????????query????????????
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //2.1?????????1?????????ID????????????
        if (!ObjectUtils.isEmpty(paramVo.getCategory1Id())) {
            boolQuery.must(
                    QueryBuilders.termQuery(SysEsConst.QUERY_CATEGORY1ID,
                            paramVo.getCategory1Id()));
        }
        //2.1?????????2?????????ID????????????
        if (!ObjectUtils.isEmpty(paramVo.getCategory2Id())) {
            boolQuery.must(
                    QueryBuilders.termQuery(SysEsConst.QUERY_CATEGORY2ID,
                            paramVo.getCategory2Id()));
        }
        //2.1?????????3?????????ID????????????
        if (!ObjectUtils.isEmpty(paramVo.getCategory3Id())) {
            boolQuery.must(
                    QueryBuilders.termQuery(SysEsConst.QUERY_CATEGORY3ID,
                            paramVo.getCategory3Id()));
        }
        //2.2??????????????????keyWord????????????
        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            boolQuery.must(
                    QueryBuilders.matchQuery(SysEsConst.QUERY_TITLE,
                            paramVo.getKeyword()));
            //??????????????????????????????
            queryBuilder.withHighlightFields(new HighlightBuilder
                    .Field(SysEsConst.QUERY_TITLE)
                    .preTags("<span style ='color:red'>")
                    .postTags("</span>"));

        }
        //2.3??????????????????????????????
        if (!StringUtils.isEmpty(paramVo.getTrademark())) {
            //2.3.1??????????????????id???
            long tmId = Long.parseLong(paramVo.getTrademark().split(":")[0]);
            boolQuery.must(
                    QueryBuilders.termQuery(
                            SysEsConst.QUERY_TMID, tmId));
        }
        //2.4??????????????????????????????
        if (!ObjectUtils.isEmpty(paramVo.getProps())) {

            Arrays.stream(paramVo.getProps())
                    .forEach(attr -> {
                        BoolQueryBuilder nestedQuery = QueryBuilders.boolQuery();

                        long attrId = Long.parseLong(attr.split(":")[0]);
                        nestedQuery.must(QueryBuilders.termQuery(
                                SysEsConst.NESTED_QUERY_PATH_ATTRS_ATTRID
                                , attrId));

                        String attrValue = attr.split(":")[1];
                        nestedQuery.must(QueryBuilders.termQuery(
                                SysEsConst.NESTED_QUERY_PATH_ATTRS_ATTRVALUE
                                , attrValue));

                        NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(SysEsConst.NESTED_QUERY_PATH_ATTRS,
                                nestedQuery,
                                ScoreMode.None);
                        boolQuery.must(nestedQueryBuilder);
                    });
        }
        queryBuilder.withQuery(boolQuery);
        //===================??????????????????=============================

        //2.5?????????order??????
        if (!StringUtils.isEmpty(paramVo.getOrder())) {
            //2.5.1??????order??????????????????orderId
            String[] split = paramVo.getOrder().split(":");
            //2.5.2??????orderId?????????????????????????????????
            String orderField = SysEsConst.ORDER_FIELD_HOTSCORE;
            switch (split[0]) {
                case "1":
                    orderField = SysEsConst.ORDER_FIELD_HOTSCORE;
                    break;
                case "2":
                    orderField = SysEsConst.ORDER_FIELD_PRICE;
                    break;
                case "3":
                    orderField = SysEsConst.ORDER_FIELD_CREATE_TIME;
                    break;
                default:
            }
            queryBuilder.withSort(SortBuilders.fieldSort(orderField).order(SysEsConst.ORDER_SORT_ASC.equals(split[1]) ? SortOrder.ASC : SortOrder.DESC));
        }
        //2.6??????????????????
        queryBuilder.withPageable(PageRequest.of(paramVo.getPageNo() - 1, SysEsConst.SEARCH_PAGE_SIZE));
        //====================??????????????????==============================

        //=====================?????????????????????DSL??????????????????????????????????????????
        //3.1?????????ID?????????
        TermsAggregationBuilder tmIdAgg = AggregationBuilders
                .terms(SysEsConst.AGG_TMID_AGG)
                .field(SysEsConst.QUERY_TMID)
                .size(100);
        //??????ID?????????????????????????????????
        tmIdAgg.subAggregation(AggregationBuilders
                .terms(SysEsConst.AGG_TM_NAME_AGG)
                .field(SysEsConst.QUERY_TM_NAME)
                .size(100));
        //??????ID??????????????????logo????????????
        tmIdAgg.subAggregation(AggregationBuilders
                .terms(SysEsConst.AGG_TM_LOGO_AGG)
                .field(SysEsConst.TM_LOGO_URL)
                .size(100));
        queryBuilder.addAggregation(tmIdAgg);

        //3.2???????????????????????????
        NestedAggregationBuilder attrNestedAgg = AggregationBuilders.nested(SysEsConst.NESTED_AGG_ATTR_AGG, SysEsConst.NESTED_QUERY_PATH_ATTRS);
        TermsAggregationBuilder atrIdAgg = AggregationBuilders.terms(SysEsConst.AGG_ATTR_ID_AGG)
                .field(SysEsConst.NESTED_QUERY_PATH_ATTRS_ATTRID)
                .size(100);

        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms(SysEsConst.AGG_ATTR_NAME_AGG)
                .field(SysEsConst.NESTED_QUERY_PATH_ATTRS_ATTRNAME)
                .size(100);

        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms(SysEsConst.AGG_ATTR_VALUE_AGG)
                .field(SysEsConst.NESTED_QUERY_PATH_ATTRS_ATTRVALUE)
                .size(100);
        atrIdAgg.subAggregation(attrValueAgg);
        atrIdAgg.subAggregation(attrNameAgg);
        attrNestedAgg.subAggregation(atrIdAgg);
        queryBuilder.addAggregation(attrNestedAgg);
        return queryBuilder.build();
    }
}
