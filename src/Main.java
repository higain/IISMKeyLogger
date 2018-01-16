import jdk.nashorn.internal.objects.NativeError;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import lc.kra.system.mouse.event.GlobalMouseListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.lang.annotation.Native;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main implements NativeKeyListener, NativeMouseInputListener {
    private static FileLogger log;
    private static HashSet<Integer> pressedkeys;


    /*public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_B) {
            System.out.print("Attempting to consume B event...\t");
            try {
                Field f = NativeInputEvent.class.getDeclaredField("reserved");
                f.setAccessible(true);
                f.setShort(e, (short) 0x01);

                System.out.print("[ OK ]\n");
            }
            catch (Exception ex) {
                System.out.print("[ !! ]\n");
                ex.printStackTrace();
            }
        }
    } */

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        log.appendToLog(System.currentTimeMillis() + " " + "Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        pressedkeys.add(e.getKeyCode());
        printPressedKeys();

        /*if ((pressedkeys.size() == 1) && (pressedkeys.contains(NativeKeyEvent.VC_ESCAPE))) {
            try {
                System.exit(0);
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        } */
        catchEvilShortcuts(e);
    }

    public void printPressedKeys() {
        System.out.println("Pressed keys: ");

        for(int i : pressedkeys) {
            System.out.print(i +" ");
        }
        System.out.println();
    }

    public void nativeKeyReleased(NativeKeyEvent e) {

        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        log.appendToLog(System.currentTimeMillis() + " " + "Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        pressedkeys.remove(e.getKeyCode());
        printPressedKeys();

        catchEvilShortcuts(e);
    }

    public void catchEvilShortcuts(NativeKeyEvent e) {
        /* TODO: Kombinationen abfangen
        *  Alt+Tab, Cmd+Q, Cmd+W, Cmd+Alt+Esc
        *  https://github.com/kwhat/jnativehook/blob/master/src/java/org/jnativehook/keyboard/NativeKeyEvent.java
        */



        if((pressedkeys.contains(NativeKeyEvent.VC_ALT) && pressedkeys.contains(NativeKeyEvent.VC_F4)) ||
                (pressedkeys.contains(NativeKeyEvent.VC_CONTROL) && pressedkeys.contains(NativeKeyEvent.VC_W)) ||
                (pressedkeys.contains(NativeKeyEvent.VC_META) && pressedkeys.contains(NativeKeyEvent.VC_Q)) ||
                (pressedkeys.contains(NativeKeyEvent.VC_META) && pressedkeys.contains(NativeKeyEvent.VC_W)) ||
                (pressedkeys.contains(NativeKeyEvent.VC_CONTROL) && pressedkeys.contains(NativeKeyEvent.VC_ALT) && pressedkeys.contains(NativeKeyEvent.VC_DELETE)) ||
                (pressedkeys.contains(NativeKeyEvent.VC_META) && pressedkeys.contains(NativeKeyEvent.VC_ALT) && pressedkeys.contains(NativeKeyEvent.VC_ESCAPE))) {
            System.out.print("Attempting to consume B event...\t");

            try {
                Field f = NativeInputEvent.class.getDeclaredField("reserved");
                f.setAccessible(true);
                f.setShort(e, (short) 0x01);

                System.out.print("[ OK ]\n");
            }
            catch (Exception ex) {
                System.out.print("[ !! ]\n");
                ex.printStackTrace();
            }
        }
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

        pressedkeys = new HashSet<>();

        try {
            GlobalScreen.setEventDispatcher(new VoidDispatchService());
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

    private static class VoidDispatchService extends AbstractExecutorService {
        private boolean running = false;

        public VoidDispatchService() {
            running = true;
        }

        public void shutdown() {
            running = false;
        }

        public List<Runnable> shutdownNow() {
            running = false;
            return new ArrayList<Runnable>(0);
        }

        public boolean isShutdown() {
            return !running;
        }

        public boolean isTerminated() {
            return !running;
        }

        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return true;
        }

        public void execute(Runnable r) {
            r.run();
        }
    }

}