package com.arthurportas.presentation;

import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arthurportas on 22/04/2017.
 */
public class App {

    private static final String WAR_NAME = "staffgest-mvc-freemarker";

    private static final int PORT_NUMBER = 8080;

    private GlassFish glassfish;

    private Deployer deployer;

    protected void setUp() throws Exception {

        GlassFishProperties props = new GlassFishProperties();
        props.setPort("http-listener", PORT_NUMBER);
        glassfish = GlassFishRuntime.bootstrap().newGlassFish(props);
        glassfish.start();
        glassfish.getDeployer();
        deployer = glassfish.getDeployer();
        ScatteredArchive archive = new ScatteredArchive(WAR_NAME, ScatteredArchive.Type.WAR, new File("src/main/webapp"));
        archive.addClassPath(new File("target", "classes"));
        deployer.deploy(archive.toURI());
    }

    protected void tearDown() throws Exception {

        deployer.undeploy(WAR_NAME);
        glassfish.dispose();
    }

    protected void test(String[] names) throws Exception {

        URL url = new URL("http://localhost:" + PORT_NUMBER + "/" + WAR_NAME + "/new-employee.html");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        System.out.println("Request url: " + url);
        String line = null;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
        conn.disconnect();
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.setUp();
        //  app.test(args);
        //  app.tearDown();
    }
}
