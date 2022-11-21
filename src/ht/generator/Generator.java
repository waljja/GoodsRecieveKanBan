package ht.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis Generator 逆向工程 （Java调用）
 * @author 丁国钊
 * @date 2022-11-19
 */
public class Generator {
    /**
     * 生成 model mapper
     */
    public void generateModelAndMapper() {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        // src/108FLConfig.xml
        // src/26imslabel.xml
        // src/33GRNewDashboard3.xml
        // src/131HonortoneMesDb.xml
        File configFile = new File("src/33GRNewDashboard3.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = null;
        try {
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            myBatisGenerator.generate(null);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new  Generator().generateModelAndMapper();
    }
}
