package com.hwua.test;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.Iterator;

public class LuceneTest {
    /**
     * 创建索引库
     *
     * @throws Exception
     */
    @Test
    public void createIndex() throws Exception {
        //1.创建索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.以写的方式打开索引库,内部使用的是默认分析器对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
        //3.找到存放文档的目录
        File dir = new File("D:\\searchsource");
        //4.遍历目录中的文件，为每个文件创建一个Document对象
        for (File file : dir.listFiles()) {
            Document document = new Document();
            //为文档创建对应的Field
            String fileName = file.getName();//获取文件名
            String filePath = file.getPath();//文件路径
            String fileContent = FileUtils.readFileToString(file, "utf-8");//获取文件内容
            long fileSize = FileUtils.sizeOf(file);//获取文件大小
            //创建一个域（key value 是否保存（只有保存的才能读出内容））
            Field fileNameField = new TextField("fileName", fileName, Field.Store.YES);
            Field filePathField = new StoredField("filePath", filePath);
            Field fileContentField = new TextField("fileContent", fileContent, Field.Store.YES);
            Field fileSizeField = new LongPoint("fileSize", fileSize);
            Field fileSizeField2 = new StoredField("fileSize", fileSize);
            document.add(fileNameField);
            document.add(filePathField);
            document.add(fileContentField);
            document.add(fileSizeField);
            document.add(fileSizeField2);
            //5. 把文档添加到写入到索引库中
            indexWriter.addDocument(document);
        }
        System.out.println("创建索引库成功！");
        indexWriter.close();

    }

    /**
     * 查询索引库
     *
     * @throws Exception
     */
    @Test
    public void queryIndex() throws Exception {
        //1.得到索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.创建IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //3.创建IndexSearcher对象来查询指定的索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //4.创建TermQuery查询对象
        TermQuery query = new TermQuery(new Term("fileContent", "化石"));
        //5.查询返回TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 5);
        //打印输出具体的查询的记录条数
        System.out.println("查询到的总条数:" + topDocs.totalHits);
        //遍历得到的Document对象
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docid = scoreDoc.doc;//得到文档的id
            Document document = indexSearcher.doc(docid);//根据文档id找到对应的Document对象
            //打印输出域中的数据
            System.out.println(document.get("fileName"));
            System.out.println(document.get("filePath"));
            System.out.println(document.get("fileSize"));
            System.out.println("-----------------------------------------------------");
        }
        indexReader.close();
    }

    /**
     * 使用中文分词器来进行中英文分词
     *
     * @throws Exception
     */
    @Test
    public void testStandardAnalyzer() throws Exception {
        //1.创建标准分析器对象
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //2.分析器开始分析
        //TokenStream tokenStream = analyzer.tokenStream("test", "Learn how to create a web page with Spring MVC.");
        TokenStream tokenStream = analyzer.tokenStream("test", "顶层牛皮皮鞋,价格实惠,性价比高.");
        //3.添加引用，通过应用获取对应的词汇单元,charTermAttribute变量存放的就是词汇单元
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();//将指针指向第一条词汇单元
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute);
        }
        tokenStream.close();

    }

    /**
     * 使使用标准分词器来进行英文分词，中文分词
     *
     * @throws Exception
     */
    @Test
    public void testIKAnalyzer() throws Exception {
        //1.创建标准分析器对象
        IKAnalyzer ikAnalyzer = new IKAnalyzer();
        //2.分析器开始分析
        //TokenStream tokenStream = analyzer.tokenStream("test", "Learn how to create a web page with Spring MVC.");
        TokenStream tokenStream = ikAnalyzer.tokenStream("test", "顶层牛皮皮鞋,价格实惠,性价比高,上海海文欢迎您.Learn how to create a web page with Spring MVC.");
        //3.添加引用，通过应用获取对应的词汇单元,charTermAttribute变量存放的就是词汇单元
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();//将指针指向第一条词汇单元
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute);
        }
        tokenStream.close();

    }

    /**
     * 使使用标准分词器来进行英文分词，中文分词
     *
     * @throws Exception
     */
    @Test
    public void testSmartChineseAnalyzer() throws Exception {

        //1.创建标准分析器对象
        Analyzer smart = new SmartChineseAnalyzer(true);
        //2.分析器开始分析
        //TokenStream tokenStream = analyzer.tokenStream("test", "Learn how to create a web page with Spring MVC.");
        TokenStream tokenStream = smart.tokenStream("test", "顶层牛皮皮鞋,价格实傻叉惠,性价和比高,上海海文欢迎您.Learn how to create a web page with Spring MVC.");
        //3.添加引用，通过应用获取对应的词汇单元,charTermAttribute变量存放的就是词汇单元
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();//将指针指向第一条词汇单元
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute);
        }
        tokenStream.close();

    }

    /**
     * 数值范围的查询
     *
     * @throws Exception
     */
    @Test
    public void testSearchRange() throws Exception {
//1.得到索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.创建IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //3.创建IndexSearcher对象来查询指定的索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //4.创建Query查询对象
        Query query = LongPoint.newRangeQuery("fileSize", 10L, 2000L);
        //5.查询返回TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 20);
        //打印输出具体的查询的记录条数
        System.out.println("查询到的总条数:" + topDocs.totalHits);
        //遍历得到的Document对象
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docid = scoreDoc.doc;//得到文档的id
            Document document = indexSearcher.doc(docid);//根据文档id找到对应的Document对象
            //打印输出域中的数据
            System.out.println(document.get("fileName"));
            System.out.println(document.get("filePath"));
            System.out.println(document.get("fileSize"));
            System.out.println("-----------------------------------------------------");
        }
        indexReader.close();
    }

    /**
     * 添加文档
     * @throws Exception
     */
    @Test
    public void addDocument() throws Exception{
        //1.创建索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.以写的方式打开索引库,内部使用的是默认分析器对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        //3.创建一个Document对象
        Document document = new Document();
        document.add(new TextField("fileName","陈豪AAAA.txt",Field.Store.YES));
        document.add(new TextField("filePath","d:\\陈豪AAAA.txt",Field.Store.YES));
        document.add(new TextField("fileContent","我爱你,我的祖国。",Field.Store.YES));
        document.add(new LongPoint("fileSize",1000L));
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 删除全部索引
     * @throws Exception
     */
    @Test
    public void deleteAllIndex() throws Exception{
        //1.创建索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.以写的方式打开索引库,内部使用的是默认分析器对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        indexWriter.deleteAll();
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 按条件删除文档
     * @throws Exception
     */
    @Test
    public void deleteIndex() throws Exception{
        //1.创建索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.以写的方式打开索引库,内部使用的是默认分析器对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        TermQuery termQuery = new TermQuery(new Term("fileContent", "apache"));
        indexWriter.deleteDocuments(termQuery);
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 更新索引(先删除再添加)
     * @throws Exception
     */
    @Test
    public void updateIndex() throws Exception{
        //1.创建索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.以写的方式打开索引库,内部使用的是默认分析器对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        Document document = new Document();
        document.add(new TextField("fileName","要更新的文档", Field.Store.YES));
        document.add(new TextField("fileContent","近日在使用lucene的过程中遇到一种现象,如中国的植物活化石银杏最早出现于3.45亿年前的石炭纪", Field.Store.YES));
        indexWriter.updateDocument(new Term("fileContent","java"),document);
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * queryParser查询
     * @throws Exception
     */
    @Test
    public void testQueryParser() throws Exception{
        //1.得到索引库的存放路径对象
        Directory directory = FSDirectory.open(new File("d:\\lucene\\index").toPath());
        //2.创建IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //3.创建IndexSearcher对象来查询指定的索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //4.创建QueryParser查询对象
        QueryParser queryParser = new QueryParser("fileContent", new IKAnalyzer());
        Query query = queryParser.parse("lucene是java开发的");
        //5.查询返回TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 10);
        //打印输出具体的查询的记录条数
        System.out.println("查询到的总条数:" + topDocs.totalHits);
        //遍历得到的Document对象
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docid = scoreDoc.doc;//得到文档的id
            Document document = indexSearcher.doc(docid);//根据文档id找到对应的Document对象
            //打印输出域中的数据
            System.out.println(document.get("fileName"));
            System.out.println(document.get("filePath"));
            System.out.println(document.get("fileSize"));
            System.out.println("-----------------------------------------------------");
        }
        indexReader.close();
    }

}
