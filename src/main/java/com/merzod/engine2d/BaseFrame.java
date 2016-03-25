package com.merzod.engine2d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BaseFrame extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(BaseFrame.class);

    private int width = 1000;
    private int height = 750;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean park = false;

    public BaseFrame() throws HeadlessException {
        setTitle("Frame 2");
        setSize(width, height);
        Container con = getContentPane();
        final Panel2D panel = new Panel2D();
        con.add(panel);
        this.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {
                panel.keyPressed(e.getKeyCode());
//                switch(e.getKeyCode())
//                {
//                    case 37:
//                        left = true;break;
//                    case 39:
//                        right = true;break;
//                    case 38:
//                        up = true;break;
//                    case 40:
//                        down = true;break;
//                    case 32:
//                        park = true;break;
//                }
            }

            public void keyReleased(KeyEvent e) {
//                switch(e.getKeyCode())
//                {
//                    case 37:
//                        left = false;break;
//                    case 39:
//                        right = false;break;
//                    case 38:
//                        up = false;break;
//                    case 40:
//                        down = false;break;
//                    case 32:
//                        park = false;break;
//                }
            }
        });
    }

    public static void main(String[] args)
    {
        BaseFrame frame = new BaseFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.show();
    }

}
