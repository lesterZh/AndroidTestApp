package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class NoteDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "greendao.note");
        addNote(schema);

        new DaoGenerator().generateAll(schema,
                "K:\\work\\AndroidTestApp\\TestAd\\app\\src\\main\\java-gen");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("name").notNull();
        note.addStringProperty("desc");
        note.addDateProperty("date");
    }
}
