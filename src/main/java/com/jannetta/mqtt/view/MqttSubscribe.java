package com.jannetta.mqtt.view;

import com.google.gson.Gson;
import com.jannetta.mqtt.model.MQTTSubscription;
import com.jannetta.mqtt.model.Result;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MqttSubscribe {
    // private static final Logger logger = LogManager.getLogger(MqttSubscribe.class.getName());
    Logger logger = LoggerFactory.getLogger(MqttSubscribe.class);

    private static String version = "0.2 2018/12/31";
    private static String filename = "MQTTSubscribe.json";
    private static JFrame mainFrame = new JFrame("MQTT Dashboard");
    private static JPanel mainPanel = new JPanel();

    /**
     * Constructor
     */
    MqttSubscribe() {

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                File[] dirs = getDirectory("./");

                for (int i = 0; i < dirs.length; i++) {
                    try {
                        File file = new File(dirs[i].getCanonicalPath() + "//.lck");
                        file.delete();
                        if (dirs[i].delete()) {
                            logger.info("file " + dirs[i].getCanonicalPath() + " deleted.");
                        } else {
                            logger.info("file " + dirs[i].getCanonicalPath() + " not deleted.");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    //dispose();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        try {
            // Read Configuration file (JSON)
            BufferedReader bufferedReader = new BufferedReader(new FileReader( filename));
            Gson gson = new Gson();
            Result result = gson.fromJson(bufferedReader, Result.class);
           // mainPanel.setLayout(new BorderLayout());
            if (result != null) {
                // For each widget declared in the configuration file
                // instantiate the widget
                for (MQTTSubscription subscription : result.getMQTTSubscriptions()) {
                    // Get the widget type you want to use to display this subscription
                    String widgetType = subscription.getWidget();
                    Class<?> clazz = null;
                    // Instantiate the widget type and add it to the dashboard
                    try {
                        clazz = Class.forName(widgetType);
                        Constructor<?> constructor = clazz.getConstructor(MQTTSubscription.class);
                        Object instance = constructor.newInstance(subscription);
                        Widget widget = (Widget) instance;
                        mainPanel.add(widget);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
//                JScrollPane sp = new JScrollPane(mainPanel);
//                sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//                sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                mainFrame.add(mainPanel);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image mqtt = toolkit.getImage(ClassLoader.getSystemResource("mqtt.png"));
                int xSize = ((int) toolkit.getScreenSize().getWidth());
                int ySize = ((int) toolkit.getScreenSize().getHeight());
                mainFrame.setIconImage(mqtt);
                mainFrame.pack();
                mainFrame.setVisible(true);
                mainFrame.setSize(1024, 768);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public void main(String[] args) {
        Options options = new Options();
        Option s = Option.builder("s").hasArg().required().desc("Subscription file in JSON format.").build();
        Option v = Option.builder("v").desc("Version.").build();
        options.addOption(s);
        options.addOption(v);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("s")) {
                filename = cmd.getOptionValue("s");
            }
            if (cmd.hasOption("s")) {
                filename = cmd.getOptionValue("s");
            }
            MqttSubscribe app = new MqttSubscribe();

        } catch (
                MissingOptionException e) {
            help(options);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Prints the help text explaining command line parameters
     *
     * @param options
     */
    private static void help(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(version + "\n"+
                "java -cp jmc.jar com.jannetta.mqtt.view.MqttSubscribe", options);
    }

    /**
     * Get all files in the directory/file names that starts with the string "paho"
     * @param dirPath The directory in which to search for directories
     * @return the directory/file names as an array of File
     */
    static public File[] getDirectory(String dirPath) {
        File f = new File(dirPath); // current directory
        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.startsWith("paho")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File[] files = f.listFiles(textFilter);
        return files;
    }

}
