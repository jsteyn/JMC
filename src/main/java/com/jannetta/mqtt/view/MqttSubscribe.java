package com.jannetta.mqtt.view;

import com.google.gson.Gson;
import com.jannetta.mqtt.model.MQTTSubscription;
import com.jannetta.mqtt.model.Result;
import org.apache.commons.cli.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.io.File;

public class MqttSubscribe {
    static String dirPath = "d://Idea//usbtemp//data//";
    static String filename = "MQTTSubscribe.json";
    static JFrame mainFrame = new JFrame("MQTT Dashboard");
    static JPanel mainPanel = new JPanel();
    static String broker = "tcp://192.168.1.6:1883";
    static String topic = "sensor/PC/temperature";
    static String label = "study";

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
                            System.out.println("file " + dirs[i].getCanonicalPath() + " deleted.");
                        } else {
                            System.out.println("file " + dirs[i].getCanonicalPath() + " not deleted.");
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader( filename));
            Gson gson = new Gson();

            Result result = gson.fromJson(bufferedReader, Result.class);

            if (result != null) {


                for (MQTTSubscription m : result.getMQTTSubscriptions()) {
                    Widget w = new Widget(m);
                    mainPanel.add(w);
                }
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
        options.addOption(s);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);
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
        formatter.printHelp("java -cp MitoModel.jar com.jannetta.view.CalcMutPredLoad\n" + "Version: 3\n"
                + "Calculate the MutPred load for all haplogrep files specified.", options);
    }

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
