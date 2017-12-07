import java.util.Map.Entry;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Main {
    private static boolean run = true;
    private static FileLogger log;

    public static void main(String[] args) {
        log = new FileLogger();
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false); // use false here to switch to hook instead of raw input
        System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown.");
        /*for (Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet())
            System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());*/

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
                System.out.println(event);
                log.appendToLog(System.currentTimeMillis() + " " + Character.toString(event.getKeyChar()) + " = " + event.toString());

                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE)
                    run = false;
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                System.out.println(event);
                log.appendToLog(System.currentTimeMillis() + " " + Character.toString(event.getKeyChar()) + " = " + event.toString());
            }
        });

        try {
            while (run) Thread.sleep(128);
        } catch (InterruptedException e) { /* nothing to do here */ } finally {
            keyboardHook.shutdownHook();
        }
    }
}