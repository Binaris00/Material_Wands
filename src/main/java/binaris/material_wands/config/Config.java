package binaris.material_wands.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {

    public static String id;
    public static String name;
    public static File file;
    public static boolean onFolder;
    public static HashMap<String, Object> config = new HashMap<String, Object>();


    public Config(String id, String name, boolean onFolder){
        Config.id = id;
        Config.name = name;
        Config.onFolder = onFolder;
    }

    public void load() throws IOException {
        if (onFolder) {
            File folder = new File("./config/" + id + "/");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            file = new File("./config/" + id + "/" + name + ".properties");
        } else {
            file = new File("./config/" + name +".properties");

            if (!file.exists()) {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                for (Map.Entry<String, Object> set : config.entrySet()) {
                    if(set.getKey().equals("Comment")){
                        writer.write("#" + set.getValue() + "\n");
                    } else if (set.getKey().equals("EmptyLine")) {
                        writer.write("\n");
                    }
                    else {
                        writer.write(set.getKey() + " = " + set.getValue() + "\n");
                    }
                }
                writer.close();
            }
            else {
                Scanner reader = new Scanner(file);

                for (int line = 1; reader.hasNextLine(); line++) {
                    if (!reader.nextLine().isEmpty() && !reader.nextLine().startsWith("#")) {
                        String[] parts = reader.nextLine().split(" = ", 2);

                        if (parts.length == 2) {
                            // Recognizes comments after a value
                            config.put(parts[0], parts[1]);
                        } else {
                            throw new RuntimeException("Syntax error in config file on line " + line + "!");
                        }
                    }
                }
            }
        }
    }

    public void set(String key, Object value){
        config.put(key, value);
    }

    public static boolean getBool(String key){
        return (boolean) config.get(key);
    }
    public static int getInt(String key){
        return (int) config.get(key);
    }
    public static double getDouble(String key){
        return (double) config.get(key);
    }
    public static String getString(String key){
        return (String) config.get(key);
    }
    public static float getFloat(String key){
        return (float) config.get(key);
    }


}
