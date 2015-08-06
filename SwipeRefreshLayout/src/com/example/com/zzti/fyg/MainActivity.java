package com.example.com.zzti.fyg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.zzti.fyg.beans.Person;
import com.zzti.fyg.widgets.SwipeRefreshLayout;
import com.zzti.fyg.widgets.SwipeRefreshLayout.OnLoadListener;
import com.zzti.fyg.widgets.SwipeRefreshLayout.OnRefreshListener;

public class MainActivity extends Activity implements OnRefreshListener,OnLoadListener{
	
	private ListView pushloglist;
	private List<Person> list;
	LogAdapter logAdapter;
	private LayoutInflater inflater;
	private String load_str;
	private SwipeRefreshLayout swipe_ly;
	private View no_msg_rl;
	private View no_RL;
	private TextView no_text;
	private boolean isRefresh = false;
	private Boolean noMoreData=false;
	private Boolean isClear=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = new ArrayList<Person>();
		inflater = LayoutInflater.from(MainActivity.this);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	


	public void init(){
		pushloglist = (ListView) findViewById(R.id.pushloglist);
		swipe_ly = (SwipeRefreshLayout)findViewById(R.id.swipe_ly);
		no_msg_rl = inflater.inflate(R.layout.no_push_msg_rl, null);
		no_RL = no_msg_rl.findViewById(R.id.no_RL);
		no_text = (TextView)no_msg_rl.findViewById(R.id.no_text);
		pushloglist.addFooterView(no_msg_rl);
		logAdapter = new LogAdapter();
		swipe_ly.setOnRefreshListener(this);  
		loadMore();
		swipe_ly.setOnLoadListener(this);
		pushloglist.setAdapter(logAdapter);
		if (isRefresh) {
			swipe_ly.setRefreshing(false,"刷新成功");  
		}else {
			swipe_ly.setLoading(false,"加载成功");
		}
	}
	
	
	public void loadMore(){
		if (isClear) {
			list.clear();
		}
		Person person;
		for (int i = 0; i < 20; i++) {
			person = new Person();
			person.setDes("这是"+i);
			person.setName(i+"");
			list.add(person);
		}
			if (list.size() == 0) {
				no_RL.setVisibility(View.VISIBLE);
			}else {
				no_RL.setVisibility(View.GONE);
			}

			if (isRefresh) {
				swipe_ly.setRefreshing(false,"刷新成功");  
			}else {
				swipe_ly.setLoading(false,"加载成功");
			}
			logAdapter.notifyDataSetChanged();

			noMoreData = true;
			if (logAdapter.getCount() == 0) {
				load_str = "抱歉,暂时没有~";
			} else {
				load_str = "到底了";
			}
			no_text.setText(load_str);
			logAdapter.notifyDataSetChanged();
	}
	
	class LogAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView ==null){
				convertView = inflater.inflate(R.layout.loglistview_item, null);
				holder = new ViewHolder();
				holder.name = (TextView)convertView. findViewById(R.id.name);
				holder.des = (TextView) convertView.findViewById(R.id.des);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(list.get(position).getName().toString());
			holder.des.setText(list.get(position).getDes().toString());
			return convertView;
		}
		
	}
	
	class ViewHolder {
		TextView name,des;
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				isRefresh = true;
				isClear = true;
				loadMore();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		if (noMoreData == true) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					swipe_ly.setLoading(false,"没有更多");
				}
			}, 1000);
		
		}else {
			 isRefresh = false;
			 loadMore();
		}
	}
}
