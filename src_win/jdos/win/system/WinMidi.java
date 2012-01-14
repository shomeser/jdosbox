package jdos.win.system;

import javax.sound.midi.*;
import java.io.File;

public class WinMidi extends WinMCI {
    private File file;
    private Sequence sequence;
    private Sequencer sequencer;

    public WinMidi(int id) {
        super(id);
    }

    public void play(int from, int to, int hWndCallback, boolean wait) {
        hWnd = hWndCallback;
        sequencer.start();
    }

    public boolean setFile(File file) {
        this.file = file;
        try {
            sequence = MidiSystem.getSequence(file);
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.addMetaEventListener(new MetaEventListener() {
                public void meta(MetaMessage meta) {
                    if ( meta.getType() == 47 ) {
                        if (hWnd != 0)
                            sendNotification(MCI_NOTIFY_SUCCESSFUL);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}