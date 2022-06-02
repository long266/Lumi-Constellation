package com.nhlong.lumi.constellation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class SettingsTools {
    public static void dispatch(String[] tool) {
        if (tool[0].contentEquals("reboot")) {
            reboot();
        } else if (tool[0].contentEquals("recovery")) {
            recovery();
        } else if (tool[0].contentEquals("download")) {
            download();
        } else if (tool[0].contentEquals("bootloader")) {
            bootloader();
        } else if (tool[0].contentEquals("hotboot")) {
            hotboot();
        } else if (tool[0].contentEquals("restartsystemui")) {
            rebootSystemUi();
        } else if (!tool[0].contentEquals("shell")) {
        } else {
            if (tool.length < 2) {
                Log.e("Tools", "Not enough parameters given for SHELL");
            } else {
                shell(tool[1]);
            }
        }
    }

    public static void dispatch(Context context, String[] tool) {
        if (tool[0].contentEquals("reboot")) {
            reboot(context);
        } else if (tool[0].contentEquals("recovery")) {
            recovery(context);
        } else if (tool[0].contentEquals("download")) {
            download(context);
        } else if (tool[0].contentEquals("bootloader")) {
            bootloader(context);
        } else if (tool[0].contentEquals("hotboot")) {
            hotboot();
        } else if (tool[0].contentEquals("restartsystemui")) {
            rebootSystemUi();
        } else if (!tool[0].contentEquals("shell")) {
        } else {
            if (tool.length < 2) {
                Log.e("Tools", "Not enough parameters given for SHELL");
            } else {
                shell(tool[1]);
            }
        }
    }

    public static void reboot() {
        rebootPhone("now");
    }

    public static void reboot(Context context) {
        rebootPhone(context, "now");
    }

    public static void recovery() {
        rebootPhone("recovery");
    }

    public static void recovery(Context context) {
        rebootPhone(context, "recovery");
    }

    public static void download() {
        rebootPhone("download");
    }

    public static void download(Context context) {
        rebootPhone(context, "download");
    }

    public static void bootloader() {
        rebootPhone("bootloader");
    }

    public static void bootloader(Context context) {
        rebootPhone(context, "bootloader");
    }

    public static void rebootSystemUi() {
        shell("pkill -TERM -f com.android.systemui");
    }

    public static void hotboot() {
        shell("setprop ctl.restart surfaceflinger;setprop ctl.restart zygote");
    }

    public static String shell(String cmd) {
        String out = "";
        Iterator it = system(getSuBin(), cmd).getStringArrayList("out").iterator();
        while (it.hasNext()) {
            out = new StringBuilder(String.valueOf(out)).append((String) it.next()).append("\n").toString();
        }
        return out;
    }

    private static void rebootPhone(String type) {
        shell("reboot " + type);
    }

    @SuppressLint("WrongConstant")
    public static void rebootPhone(Context context, String type) {
        try {
            ((PowerManager) context.getSystemService("power")).reboot(type);
        } catch (Exception e) {
            Log.e("Tools", "reboot '" + type + "' error: " + e.getMessage());
            shell("reboot " + type);
        }
    }

    private static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static String getSuBin() {
        if (new File("/system/fbin", "fu").exists()) {
            return "/system/fbin/fu";
        }
        return "sh";
    }

    private static Bundle system(String shell, String command) {
        ArrayList<String> res = new ArrayList();
        ArrayList<String> err = new ArrayList();
        boolean success = false;
        try {
            Process process = Runtime.getRuntime().exec(shell);
            DataOutputStream STDIN = new DataOutputStream(process.getOutputStream());
            BufferedReader STDOUT = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader STDERR = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            STDIN.writeBytes(new StringBuilder(String.valueOf(command)).append("\n").toString());
            STDIN.flush();
            STDIN.writeBytes("exit\n");
            STDIN.flush();
            process.waitFor();
            if (process.exitValue() == 255) {
                err.add("SU was probably denied! Exit valie is 255");
            }
            while (STDOUT.ready()) {
                res.add(STDOUT.readLine());
            }
            while (STDERR.ready()) {
                err.add(STDERR.readLine());
            }
            process.destroy();
            success = true;
            if (err.size() > 0) {
                success = false;
            }
        } catch (IOException e) {
            err.add("IOException: " + e.getMessage());
        } catch (InterruptedException e2) {
            err.add("InterruptedException: " + e2.getMessage());
        }
        Bundle r = new Bundle();
        r.putBoolean("success", success);
        r.putString("cmd", command);
        r.putString("binary", shell);
        r.putStringArrayList("out", res);
        r.putStringArrayList("error", err);
        return r;
    }
}
