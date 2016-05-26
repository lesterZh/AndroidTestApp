package com.zhhtao.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhhtao.testad.R;
import com.zhhtao.utils.FormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import greendao.note.DaoMaster;
import greendao.note.DaoSession;
import greendao.note.Note;
import greendao.note.NoteDao;

public class GreenDaoTestActivity extends BaseActivty {

    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    NoteDao noteDao;
    @InjectView(R.id.et_input)
    EditText etInput;
    @InjectView(R.id.btn_add)
    Button btnAdd;
    @InjectView(R.id.btn_query)
    Button btnQuery;
    @InjectView(R.id.btn_update)
    Button btnUpdate;
    @InjectView(R.id.btn_delete)
    Button btnDelete;
    @InjectView(R.id.lv_datas)
    ListView lvDatas;
    MyAdapter myAdapter;

    String input;
    List<Note> notesList = new ArrayList<>();
    List<Note> queryList = new ArrayList<>();
    @InjectView(R.id.btn_delete_all)
    Button btnDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao_test);
        ButterKnife.inject(this);

        initDb();


        myAdapter = new MyAdapter(mContext, 0, notesList);

        lvDatas.setAdapter(myAdapter);

    }

    private void initDb() {
        //这些初始化最后放在application中进行，一个APP持有一个daoSession
        DaoMaster.DevOpenHelper devOpenHelper = new
                DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();


        noteDao = daoSession.getNoteDao();
    }


    public void addNote(Note note) {
        noteDao.insert(note);
    }

    @OnClick({R.id.btn_add, R.id.btn_query, R.id.btn_update, R.id.btn_delete})
    public void onClick(View view) {
        input = etInput.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_add:

                addNote(new Note(null, input, "desc", new Date()));

                break;
            case R.id.btn_query:
                if (input == null || input.equals("")) {
                    queryList = noteDao.queryBuilder().list();
                } else {
                    queryList =
                            noteDao.queryBuilder()
                                    .where(NoteDao.Properties.Name.eq(input))
                                    .list();
                }

                notesList.clear();
                notesList.addAll(queryList);
                myAdapter.notifyDataSetChanged();

                break;
            case R.id.btn_update:
                // Cannot update entity without key - was it inserted before?
                Note n2 = new Note((long) Integer.parseInt(input), "new name", "desc", new Date());
                noteDao.update(n2);
                break;
            case R.id.btn_delete:
                noteDao.deleteByKey((long) Integer.parseInt(input));
                break;
        }
        etInput.setText("");
    }

    @OnClick(R.id.btn_delete_all)
    public void btnDeleteAll() {
        noteDao.deleteAll();

    }


    class MyAdapter extends ArrayAdapter<Note> {

        public MyAdapter(Context context, int resource, List<Note> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_list_test, null);
            }

            TextView text = (TextView) convertView.findViewById(R.id.tv_desc);
            text.setText(noteToString(getItem(position)));

            return convertView;
        }
    }

    public String noteToString(Note note) {
        return "Note{" +
                "id=" + note.getId() +
                ", name='" + note.getName() + '\'' +
                ", desc='" + note.getDesc() + '\'' +
                ", date=" + FormatUtil.getDateTime(note.getDate().getTime()) +
                '}';
    }
}
