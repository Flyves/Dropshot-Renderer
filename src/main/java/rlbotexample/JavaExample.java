package rlbotexample;

import rlbot.manager.BotManager;
import util.PortReader;
import util.game_constants.RlConstants;
import util.tinysound.Sound;
import util.tinysound.TinySound;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * See JavaAgent.py for usage instructions.
 *
 * Look inside SampleBot.java for the actual bot logic!
 */
public class JavaExample implements ChangeListener {

    private static final Integer DEFAULT_PORT = 17357;
    private static Sound sfxChangedSoundSample;

    public static void main(String[] args) {
        BotManager botManager = new BotManager();
        Integer port = PortReader.readPortFromArgs(args).orElseGet(() -> {
            System.out.println("Could not read port from args, using default!");
            return DEFAULT_PORT;
        });

        botManager.setRefreshRate((int)RlConstants.BOT_REFRESH_RATE);

        SamplePythonInterface pythonInterface = new SamplePythonInterface(port, botManager);
        new Thread(pythonInterface::start).start();

        JFrame frame = new JFrame("Java Bot Handler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JSlider soundVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        soundVolume.addChangeListener(new JavaExample());
        soundVolume.setMajorTickSpacing(10);
        soundVolume.setMinorTickSpacing(1);
        soundVolume.setPaintTicks(true);
        soundVolume.setPaintLabels(true);

        JPanel panel = new JPanel();

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        BorderLayout borderLayout = new BorderLayout();
        panel.setLayout(borderLayout);

        // text
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        dataPanel.add(new JLabel("Listening on port " + port), BorderLayout.CENTER);
        dataPanel.add(new JLabel("I'm the thing controlling the Java bot, keep me open :)"), BorderLayout.CENTER);
        JLabel botsRunning = new JLabel("Bots running: ");
        dataPanel.add(botsRunning, BorderLayout.CENTER);

        // sound slider
        dataPanel.add(new JLabel("."));
        dataPanel.add(new JLabel("."));
        dataPanel.add(new JLabel("."));
        dataPanel.add(new JLabel("In-game SFX volume :"));
        dataPanel.add(soundVolume);

        panel.add(dataPanel, BorderLayout.CENTER);
        frame.add(panel);

        URL url = JavaExample.class.getClassLoader().getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);
        panel.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        frame.setIconImage(image);

        frame.pack();
        frame.setVisible(true);

        ActionListener myListener = e -> {
            Set<Integer> runningBotIndices = botManager.getRunningBotIndices();

            String botsStr;
            if (runningBotIndices.isEmpty()) {
                botsStr = "None";
            } else {
                botsStr = runningBotIndices.stream()
                        .sorted()
                        .map(i -> "#" + i)
                        .collect(Collectors.joining(", "));
            }
            botsRunning.setText("Bots indices running: " + botsStr);
        };

        new Timer(1000, myListener).start();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        int fps = source.getValue();
        //GameSoundGlobals.sfxVolume = fps/10.0;
        //TinySound.setGlobalVolume(GameSoundGlobals.sfxVolume);
        if (!source.getValueIsAdjusting()) {
            TinySound.init();
            //sfxChangedSoundSample = TinySound.loadSound(GameSoundFiles.pewpew_electric_balls[0]);
            sfxChangedSoundSample.play(0.15);
        }
    }
}
