package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.MetDao;
import fr.kougteam.myCellar.tools.FontTools;

public class ListeMetsActivity extends Activity {
		
	private MetDao metDao;
	private SimpleCursorAdapter metAdapter;
	private ListView metsListView;
	
	private Cursor selectedItem;
	private Intent intent2Vin;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_mets);
		
		metDao = new MetDao(this);
		
		intent2Vin = new Intent(this, AccordMetVinActivity.class);
		
		Cursor metCursor = metDao.getAll();
		String[] from = new String[] { MetDao.COL_NOM};
		int[] to = new int[] { R.id.listeMetsItemText};
		metAdapter = new SimpleCursorAdapter(this, R.layout.liste_mets_item, metCursor, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
            	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                if(convertView == null) FontTools.setDefaultAppFont(view, getAssets());
                return view;
            }
        };
	    metsListView = (ListView)findViewById(R.id.listeMetsListView);	 
	    
	    metsListView.setAdapter(metAdapter);
	    metsListView.setFastScrollEnabled(true);
	    metsListView.setTextFilterEnabled(true);
	    metsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		    	selectedItem = (Cursor)parent.getItemAtPosition(pos);
		    	int idMet = selectedItem.getInt(selectedItem.getColumnIndex(MetDao.COL_ID));
		    	intent2Vin.putExtra("idMet", idMet);
				startActivity(intent2Vin);
			}	    
		});
	    
	    EditText etext = (EditText)findViewById(R.id.listeMetsInputSearch);
	    etext.addTextChangedListener(new TextWatcher() {
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        }

	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	        }

	        public void afterTextChanged(Editable s) {
	            SimpleCursorAdapter filterAdapter = (SimpleCursorAdapter)metsListView.getAdapter();
	            filterAdapter.getFilter().filter(s.toString());
	        }
	    });

	    metAdapter.setFilterQueryProvider(new FilterQueryProvider() {
	        public Cursor runQuery(CharSequence constraint) {
	        	if (constraint!=null) {
	    			return metDao.findMets(constraint.toString());
	    		} else {
	    			return metDao.getAll();
	    		}
	        }
	    });
	}
	
	@Override
	protected void onDestroy() {
		metDao.close();
		super.onDestroy();	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	

}
