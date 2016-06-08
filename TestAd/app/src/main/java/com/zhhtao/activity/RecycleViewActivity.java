package com.zhhtao.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zhhtao.base.BaseActivty;
import com.zhhtao.other.DividerGridItemDecoration;
import com.zhhtao.other.DividerItemDecoration;
import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;
import com.zhhtao.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecycleViewActivity extends BaseActivty {

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.my_recycle_view)
    RecyclerView myRecycleView;

    List<String> list = new ArrayList<>();

    MyRecycleAdapter adapter;
    MyRecycleMultipeAdapter multipeAdapter;
    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.btn_remove)
    Button btnRemove;

    int selectedPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        ButterKnife.bind(this);

        for (int i = 0; i <= 200; i++) {
            list.add("数据 " + i);
        }

        for (int i = 0; i <= 50; i++) {
            Msg msg = new Msg();
            if (i % 3 == 0) {
                msg.type = Msg.TYPE_FROM;
                msg.message = "from"+i;
            } else {
                msg.type = Msg.TYPE_TO;
                msg.message = "to"+i;
            }
            msgList.add(msg);
        }

//        listView.setAdapter(new MyListViewAdapter(mContext, 0, list));



        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直
//        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//水平
        myRecycleView.setLayoutManager(mLinearLayoutManager);//线性布局，
//        myRecycleView.setLayoutManager(new GridLayoutManager(this, 4));//grid布局

        //瀑布流布局 4列
//        myRecycleView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        //4行
//        myRecycleView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));


        adapter = new MyRecycleAdapter();
        multipeAdapter = new MyRecycleMultipeAdapter();

        myRecycleView.setAdapter(adapter);//一种布局类型的adapter
//        myRecycleView.setAdapter(multipeAdapter);//多种类型布局的adapter

        myRecycleView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));//线性布局的分割线
//        myRecycleView.addItemDecoration(new DividerGridItemDecoration(mContext));//gird布局的分割线
        myRecycleView.setItemAnimator(new DefaultItemAnimator());

    }

    @OnClick({R.id.btn_add, R.id.btn_remove})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                adapter.addData(selectedPosition);
                break;
            case R.id.btn_remove:
                adapter.removeData(selectedPosition);
                break;
        }
    }


    List<Msg> msgList = new ArrayList<>();
    class MyRecycleMultipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == Msg.TYPE_TO) {
                return new ToViewHolder(
                        LayoutInflater
                                .from(mContext)
                                .inflate(R.layout.item_list_to, parent, false));
            } else if (viewType == Msg.TYPE_FROM){
                return new FromViewHolder(
                        LayoutInflater
                                .from(mContext)
                                .inflate(R.layout.item_list_from, parent, false));
            } else if (viewType == Msg.TYPE_HEAD) {
                return new HeadViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_list_head, parent, false));
            } else if (viewType == Msg.TYPE_BOTTOM) {
                return new BottomViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_list_bottom, parent, false));
            }
            return  null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof FromViewHolder) {
                ((FromViewHolder) holder).textView.setText(msgList.get(position).message);
            } else if (holder instanceof ToViewHolder) {
                ((ToViewHolder) holder).textView.setText(msgList.get(position).message);
            }
        }

        @Override
        public int getItemCount() {
//            return msgList.size();
            return msgList.size() + 1;//+1为了显示底部view
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return Msg.TYPE_HEAD;//头部view
            if (position == msgList.size()) return  Msg.TYPE_BOTTOM;//底部view,需要修改getItemCount
            return msgList.get(position).type ;//此时msgList中下标为0的数据不被显示
        }


        class FromViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public FromViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_desc);
            }
        }

        class ToViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public ToViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_desc);
            }
        }

        class HeadViewHolder extends RecyclerView.ViewHolder {
            public HeadViewHolder(View itemView) {
                super(itemView);
            }
        }

        class BottomViewHolder extends RecyclerView.ViewHolder {
            public BottomViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_test,
                    parent, false);

            //下面2行代码为瀑布流的布局设置随机高度
            int height = (int) (100 + Math.random()*100);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

            //最好在这里给view设置点击事件，提供外部设置事件响应的接口
            MyViewHolder viewHolder = new MyViewHolder(view);
            LogUtil.i("onCreateViewHolder"+viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_desc);

                //可以在这里设置点击监听事件
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.showToast(mContext, "click Position:" + getAdapterPosition());
                        selectedPosition = getAdapterPosition();
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        adapter.removeData(getAdapterPosition());
                        return true;
                    }
                });
            }
        }

        public void addData(int position) {
            list.add(position, "insert one");
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    class MyListViewAdapter extends ArrayAdapter<String> {

        public MyListViewAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogUtil.i("getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_test, null, false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.tv_desc);
            textView.setText(getItem(position));

            return convertView;
        }
    }
}

class Msg {
    static final int TYPE_FROM = 0;
    static final int TYPE_TO = 1;
    static final int TYPE_HEAD = 2;
    static final int TYPE_BOTTOM = 3;

    int type;
    String message;
}
