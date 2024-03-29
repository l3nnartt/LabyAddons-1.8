package com.github.l3nnartt.labyaddons;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Locale;

public class Main {
    private static final HashMap<String, String> lang = new HashMap<>();
    private static Icon icon;

    public static void main(String[] args) {
        initLookAndFeel();
        initLanguage();
        try {
            String dir = initDirectory();
            if (!new File(dir).exists()) {
                throw new IOException("No .minecraft/LabyMod directory found!");
            }

            if (showConfirmDialog(String.format(lang.get("installation"), "2.0"))) {
                File run = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                if (run.exists() && run.isFile()) {
                    for (String version : new String[]{"1.8"}) {
                        File addonsDir = new File(dir + "addons-" + version);
                        addonsDir.mkdirs();
                        File mod = new File(addonsDir, "LabyAddons.jar");
                        if (!mod.exists()) {
                            for (File addons : addonsDir.listFiles()) {
                                if (addons.getName().toLowerCase().contains("LabyAddons")) {
                                    addons.delete();
                                }
                            }
                        }

                        Files.copy(run.toPath(), mod.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    showMessageDialog(lang.get("success"), 1);
                } else {
                    throw new IOException("Invalid path: " + run.getAbsolutePath());
                }
            }
        } catch (FileSystemException e) {
            e.printStackTrace();
            if (e.getReason() != null && !e.getReason().isEmpty()) {
                showMessageDialog(lang.get("closed") + "\n" + e.getReason(), 0);
            } else {
                showMessageDialog(lang.get("error"), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessageDialog(lang.get("error"), 0);
        }
    }

    private static void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            URL url = Main.class.getResource("/assets/minecraft/labyaddons/Logo.png");
            icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(64, 64, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String initDirectory() {
        String dir = System.getenv("APPDATA") + "/.minecraft/LabyMod/";
        if (!new File(dir).exists()) {
            dir = System.getProperty("user.home") + "/Library/Application Support/minecraft/LabyMod/";
        }

        if (!new File(dir).exists()) {
            dir = System.getProperty("user.home") + "/.minecraft/LabyMod/";
        }

        return dir;
    }

    private static void initLanguage() {
        if (Locale.getDefault().toString().toLowerCase().contains("de")) {
            lang.put("installation",
                    "LabyAddons v%s kann nun installiert werden!\nMinecraft muss bei der Installation geschlossen sein!");
            lang.put("success", "LabyAddons Installation abgeschlossen!");
            lang.put("closed", "Ist Minecraft geschlossen?");
            lang.put("error",
                    "Installation fehlgeschlagen!\nKopiere das Addon in das Verzeichnis .minecraft/LabyMod/addons und starte Minecraft!");
        } else if (Locale.getDefault().toString().toLowerCase().contains("es")) {
            lang.put("installation",
                    "¡Se puede instalar el LabyAddons v%s ahora!\n¡Minecraft tiene que estar cerrador para instalarlo!");
            lang.put("success", "n¡La instalación del LabyAddons a terminado!");
            lang.put("closed", "¿Minecraft está cerrado?");
            lang.put("error",
                    "¡La instalación falló!\n¡Copiá el Addon en la carpeta .minecraft/LabyMod/addons y empezá a jugar!");
        } else if (Locale.getDefault().toString().toLowerCase().contains("pt")) {
            lang.put("installation",
                    "O LabyAddons v%s está pronto para instalação!\nFeche o Minecraft antes de continuar!");
            lang.put("success", "Instalação do LabyAddons concluída!");
            lang.put("closed", "O Minecraft está fechado?");
            lang.put("error",
                    "Instalação falhou!\nCopie os arquivos para a pasta .minecraft/LabyMod/addons e abra o Minecraft!");
        } else {
            lang.put("installation",
                    "LabyAddons v%s is now ready for installation!\nClose Minecraft before continuing!");
            lang.put("success", "LabyAddons installation finished!");
            lang.put("closed", "Is Minecraft closed?");
            lang.put("error",
                    "Installation failed!\nCopy the addon into .minecraft/LabyMod/addons and start Minecraft!");
        }
    }

    private static boolean showConfirmDialog(String msg) {
        return JOptionPane.showConfirmDialog(null, msg, "LabyAddons \u00a9 L3nnart_", 2, 1, icon) == 0;
    }

    private static void showMessageDialog(String msg, int mode) {
        JOptionPane.showMessageDialog(null, msg, "LabyAddons \u00a9 L3nnart_", mode);
    }
}

// Provided by CosmeticsMod