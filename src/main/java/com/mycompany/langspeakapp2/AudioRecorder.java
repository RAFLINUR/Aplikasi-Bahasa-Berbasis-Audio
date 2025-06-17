/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.langspeakapp2;

/**
 *
 * @author ACER
 */
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {
    private TargetDataLine targetLine;
    private File audioFile;
    private Thread recordingThread;

    public AudioRecorder(String filename) {
        this.audioFile = new File(filename);
    }

    // Memulai perekaman audio
    public void startRecording(Runnable onStart, Runnable onError) {
        new Thread(() -> {
            try {
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                targetLine = (TargetDataLine) AudioSystem.getLine(info);
                targetLine.open(format);
                targetLine.start();

                AudioInputStream audioStream = new AudioInputStream(targetLine);
                recordingThread = new Thread(() -> {
                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
                    } catch (IOException e) {
                        if (onError != null) onError.run();
                    }
                });
                recordingThread.start();
                if (onStart != null) onStart.run();
            } catch (LineUnavailableException ex) {
                if (onError != null) onError.run();
            }
        }).start();
    }

    // Menghentikan perekaman audio
    public void stopRecording() {
        if (targetLine != null && targetLine.isRunning()) {
            targetLine.stop();
            targetLine.close();
        }
    }

    // Memutar file audio hasil rekaman
    public void playRecording(Runnable onStart, Runnable onFinish, Runnable onError) {
        new Thread(() -> {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                if (onStart != null) onStart.run();
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        if (onFinish != null) onFinish.run();
                    }
                });
            } catch (Exception e) {
                if (onError != null) onError.run();
            }
        }).start();
    }

    // Format audio: mono, 16-bit, 16kHz
    private AudioFormat getAudioFormat() {
        return new AudioFormat(16000, 16, 1, true, true);
    }

    // Mengembalikan file audio
    public File getAudioFile() {
        return audioFile;
    }
}
