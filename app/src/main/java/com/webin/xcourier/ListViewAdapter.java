package com.webin.xcourier;


import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{

	public ArrayList<HashMap<String, String>> list;
	Activity activity;

	public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
		super();
		this.activity=activity;
		this.list=list;
	}
	
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
		return 0;
	}

	private class ViewHolder{
		TextView txtFirst;
		TextView txtSecond;
		TextView txtThird;
        ImageButton btnMapa;
        ImageButton btnGrabar;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	
		ViewHolder holder;
		
		LayoutInflater inflater=activity.getLayoutInflater();
		
		if(convertView == null){
			
			convertView=inflater.inflate(R.layout.colmn_row, null);
			holder=new ViewHolder();
			
			holder.txtFirst=(TextView) convertView.findViewById(R.id.TextFirst);
			holder.txtSecond=(TextView) convertView.findViewById(R.id.TextSecond);
			holder.txtThird=(TextView) convertView.findViewById(R.id.TextThird);
            holder.btnMapa=(ImageButton) convertView.findViewById(R.id.BtnMapa);
            holder.btnGrabar=(ImageButton) convertView.findViewById(R.id.BtnGrabar);

            holder.btnMapa.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = new Intent(v.getContext(), MapsActivity.class);  // get a valid context
                    i.putExtra("codigo", "RN026569386CO");  //I don't know where sign came from
                    i.putExtra("latitud", "-77.0621286000");
                    i.putExtra("longitud", "-11.8618698000");
                    v.getContext().startActivity(i);
                };
            });

            holder.btnGrabar.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = new Intent(v.getContext(), ConfirmaRemitocontroller.class);  // get a valid context
                    i.putExtra("codigo", "RN026569386CO");
                    v.getContext().startActivity(i);
                };
            });

			convertView.setTag(holder);
		}else{
			
			holder=(ViewHolder) convertView.getTag();
		}
		
		HashMap<String, String> map=list.get(position);
		holder.txtFirst.setText(map.get("First"));
		holder.txtSecond.setText(map.get("Second"));
		holder.txtThird.setText(map.get("Third"));
        if ("1".equals(map.get("Fourth"))){
            holder.btnMapa.setVisibility(View.INVISIBLE);
        }
        if ("1".equals(map.get("Cinco"))){
            holder.btnGrabar.setVisibility(View.INVISIBLE);
        }
		return convertView;
	}



}
