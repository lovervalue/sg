package com.sofn.bigdata.service.impl;

import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.Product;
import com.sofn.bigdata.service.LuceneService;
import com.sofn.bigdata.utils.PageQuery;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/3/8 13:53
 * @Description
 */
@Service
public class LuceneServiceImpl implements LuceneService {

    @Autowired
    private IndexWriter indexWriter;

    @Override
    public void createProductIndex(List<Product> productList) throws IOException {
        List<Document> docs = new ArrayList<Document>();
        for (Product p : productList) {
            //开始对文档进行逗号分隔，存入索引
            String[] s=readFileContent(p.getSkuName()).split("，");
            for(int i=0;i<s.length;i++) {
                Document doc = new Document();
                doc.add(new StringField("id", p.getId() + "", Field.Store.YES));
                //文档段落核心部分
                doc.add(new TextField("skuName", s[i], Field.Store.YES));
                doc.add(new TextField("cid1Name", p.getCid1Name(), Field.Store.YES));
                doc.add(new TextField("clickurl", p.getClickurl(), Field.Store.YES));

                Double price = p.getPrice();
                doc.add(new DoublePoint("price", price));
                doc.add(new StoredField("price", p.getPrice()));
                doc.add(new DoublePoint("discount", p.getDiscount()));
                doc.add(new StoredField("discount", p.getDiscount()));
                doc.add(new DoublePoint("discountRate", p.getDiscountRate()));
                doc.add(new StoredField("discountRate", p.getDiscountRate()));
                doc.add(new DoublePoint("cid1", p.getCid1()));
                // 正排索引用于排序、聚合
                doc.add(new DoubleDocValuesField("price", price));
                // 存储到索引库
                doc.add(new StoredField("cid1", p.getCid1()));
                docs.add(doc);
            }
        }
        indexWriter.addDocuments(docs);
        // indexWriter.updateDocument(docs);
        indexWriter.commit();
        //  indexWriter.close();
    }

    @Autowired
    private Analyzer analyzer;

    @Autowired
    private SearcherManager searcherManager;

    @Override
    public PageQuery searchProduct(PageQuery pageQuery) throws Exception, ParseException {
        searcherManager.maybeRefresh();
        IndexSearcher indexSearcher = searcherManager.acquire();
//        Analyzer analyzer = new IKAnalyzer(true);
//        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(LuceneConfig.LUCENEINDEXPATH)));
//        //索引查询器
//        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Product params = pageQuery.getParams();
        Map<String, String> queryParam = pageQuery.getQueryParam();
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Sort sort = new Sort();
        // 排序规则
        com.sofn.bigdata.utils.Sort sort1 = pageQuery.getSort();
        if (sort1 != null && sort1.getOrder() != null) {
            if ("ASC".equals(sort1.getOrder().toUpperCase())) {
                sort.setSort(new SortField(sort1.getField(), SortField.Type.FLOAT, false));
            } else if ("DESC".equals((sort1.getOrder()).toUpperCase())) {
                sort.setSort(new SortField(sort1.getField(), SortField.Type.FLOAT, true));
            }
        }

        // 模糊匹配,匹配词
        String keyStr = params.getSkuName();
        if (params != null && params.getSkuName() != null) {
            // 输入空格,不进行模糊查询
            if (!"".equals(keyStr.replaceAll(" ", ""))) {
                builder.add(new QueryParser("skuName", analyzer).parse(keyStr), BooleanClause.Occur.MUST);
            }
        }

        // 精确查询
        if (params != null && params.getCid1() != null) {
            builder.add(new TermQuery(new Term("cid1", String.valueOf(params.getCid1()))), BooleanClause.Occur.MUST);
        }
//        if (params != null && params.getGetEndTimeForL() != null) {
//            builder.add(new TermQuery(new Term("cid1", String.valueOf(params.getCid1()))), BooleanClause.Occur.MUST);
//        }
//        if (queryParam.get("lowerPrice") != null && queryParam.get("upperPrice") != null) {
//            // 价格范围查询
//            builder.add(FloatPoint.newRangeQuery("price", Float.parseFloat(queryParam.get("lowerPrice")),
//                    Float.parseFloat(queryParam.get("upperPrice"))), BooleanClause.Occur.MUST);
//        }
        PageInfo pageInfo = pageQuery.getPageInfo();
        TopDocs topDocs = indexSearcher.search(builder.build(), pageInfo.getPageNum() * pageInfo.getPageSize(), sort);

        pageInfo.setTotal(topDocs.totalHits.value);
        ScoreDoc[] hits = topDocs.scoreDocs;
        List<Product> pList = new ArrayList<Product>();
        for (int i = 0; i < hits.length; i++) {
            Document doc = indexSearcher.doc(hits[i].doc);
            Product Product = new Product();
            Product.setId(Integer.parseInt(doc.get("id")));
            Product.setSkuName(doc.get("skuName"));
            Product.setCid1(Long.valueOf(doc.get("cid1")));
            Product.setCid1Name(doc.get("cid1Name"));
            Product.setCid1Name(doc.get("cid1Name"));
            Product.setUrl(doc.get("url"));
            Product.setMaterialUrl(doc.get("materialUrl"));
            Product.setClickurl(doc.get("clickurl"));
            Product.setPrice(Double.valueOf(doc.get("price")));
            Product.setCommissionShare(doc.get("commissionShare") != null ?Double.valueOf(doc.get("commissionShare")):null);
            Product.setDiscount(doc.get("discount") != null ? Double.valueOf(doc.get("discount")):null);
            Product.setDiscountRate(doc.get("discountRate") != null ? Double.valueOf(doc.get("discountRate")):null);
            Product.setPingouPrice(doc.get("pingouPrice") != null ? Double.valueOf(doc.get("pingouPrice")):null);
            Product.setLink(doc.get("link"));
            pList.add(Product);
        }
        pageQuery.setPageInfo(pageInfo);
        pageQuery.setResults(pList);
        return pageQuery;
    }

    @Override
    public void deleteProductIndexById(String id) throws IOException {
        indexWriter.deleteDocuments(new Term("id",id));
        indexWriter.commit();
    }

    /**
     * 读取文件并且存入index
     */
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}