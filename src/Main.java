import lc.kra.system.mouse.event.GlobalMouseEvent;
import lc.kra.system.mouse.event.GlobalMouseListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main implements NativeKeyListener, NativeMouseInputListener {
    private static FileLogger log;

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        log.appendToLog(System.currentTimeMillis() + " " + "Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        log.appendToLog(System.currentTimeMillis() + " " + "Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
        log.appendToLog(System.currentTimeMillis() + " " + "Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    public void nativeMousePressed(NativeMouseEvent e) {
        System.out.println("Mouse Pressed: " + e.getButton() + " at " + e.getX() + ";" + e.getY());
        log.appendToLog(System.currentTimeMillis() + " " + "Mouse Pressed: " + e.getButton() + " at " + e.getX() + ";" + e.getY());
    }


    public static void main(String[] args) {
        log = new FileLogger();

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        Main starter = new Main();
        GlobalScreen.addNativeKeyListener(starter);
        GlobalScreen.addNativeMouseListener(starter);
        GlobalScreen.addNativeMouseMotionListener(starter);
    }

    //Nicht implementierte Methoden
    // @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        // System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
        log.appendToLog(System.currentTimeMillis() + " " + "Mouse Moved: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        // System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
        log.appendToLog(System.currentTimeMillis() + " " + "Mouse Dragged: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
    }
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
    }
}