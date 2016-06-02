package zht.test;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.avoscloud.leanchatlib_demo.ContactFragment;
import com.avoscloud.leanchatlib_demo.ConversationFragment;
import com.avoscloud.leanchatlib_demo.ConversationFragmentUpdateEvent;
import com.avoscloud.leanchatlib_demo.PersonalProfileFragment;
import com.avoscloud.leanchatlib_demo.R;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 主页面，包含三个 fragment，会话、联系人、我
 */
public class ChatActivity extends AVBaseActivity {

  private Toolbar toolbar;
  private ViewPager viewPager;
  private TabLayout tabLayout;

  private FrameLayout ftContainer;
  MyConversationFragment conversationFragment;

  /**
   * 上一次点击 back 键的时间
   * 用于双击退出的判断
   */
  private static long lastBackTime = 0;

  /**
   * 当双击 back 键在此间隔内是直接触发 onBackPressed
   */
  private final int BACK_INTERVAL = 1000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_chat);
    viewPager = (ViewPager)findViewById(R.id.pager);
    tabLayout = (TabLayout)findViewById(R.id.tablayout);

    ftContainer = (FrameLayout) findViewById(R.id.ft_container);
    conversationFragment = new MyConversationFragment();

    getSupportFragmentManager().beginTransaction()
            .replace(R.id.ft_container, conversationFragment)
            .commit();

    EventBus.getDefault().post(new ConversationFragmentUpdateEvent());
//    initTabLayout();
  }


}
