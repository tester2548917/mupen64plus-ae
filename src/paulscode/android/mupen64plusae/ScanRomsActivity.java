package paulscode.android.mupen64plusae;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mupen64plusae.v3.alpha.R;

import paulscode.android.mupen64plusae.dialog.Prompt;
import paulscode.android.mupen64plusae.util.FileUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class ScanRomsActivity extends AppCompatActivity implements OnItemClickListener
{
    private List<CharSequence> mNames;
    private List<String> mPaths;
    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private CheckBox mCheckBox3;
    private Button mCancelButton;
    private Button mOkButton;
    
    private File mCurrentPath;
 
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        
        // Pick the root of the storage directory by default
        mCurrentPath = new File( Environment.getExternalStorageDirectory().getAbsolutePath() );
        
        setContentView(R.layout.scan_roms_activity);
                
        // Set checkbox state
        mCheckBox1 = (CheckBox) findViewById( R.id.checkBox1 );
        mCheckBox2 = (CheckBox) findViewById( R.id.checkBox2 );
        mCheckBox3 = (CheckBox) findViewById( R.id.checkBox3 );
        mCheckBox1.setChecked( true );
        mCheckBox2.setChecked( true );
        mCheckBox3.setChecked( true );
        
        mCancelButton = (Button) findViewById( R.id.buttonCancel );
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScanRomsActivity.this.setResult(RESULT_CANCELED, null);
                ScanRomsActivity.this.finish();
            }
        });
        
        mOkButton = (Button) findViewById( R.id.buttonOk );
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(ActivityHelper.Keys.SEARCH_PATH, mCurrentPath.getPath());
                ScanRomsActivity.this.setResult(RESULT_OK, data);
                ScanRomsActivity.this.finish();
            }
        });

        PopulateFileList();
        
        // Create the dialog
        setTitle( mCurrentPath.getPath() );
    }
    
    private void PopulateFileList()
    {
        // Populate the file list
        // Get the filenames and absolute paths
        mNames = new ArrayList<CharSequence>();
        mPaths = new ArrayList<String>();
        FileUtil.populate( mCurrentPath, true, true, true, mNames, mPaths );
        ListView listView1 = (ListView) findViewById( R.id.listView1 );
        ArrayAdapter<String> adapter = Prompt.createFilenameAdapter( this, mPaths, mNames );
        listView1.setAdapter( adapter );
        listView1.setOnItemClickListener( this );
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        mCurrentPath = new File(mPaths.get( position ));
        PopulateFileList();
    }
}
