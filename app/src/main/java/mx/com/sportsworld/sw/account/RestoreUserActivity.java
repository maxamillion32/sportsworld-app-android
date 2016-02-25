package mx.com.sportsworld.sw.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.pojo.UserPojo;
import mx.com.sportsworld.sw.web.task.CreateUserTask;
import mx.com.sportsworld.sw.web.task.RestoreUserTask;

public class RestoreUserActivity extends Activity implements View.OnClickListener {

    private Spinner ddlIdClub;
    private EditText txtNumMembresia;
    private EditText txtCorreoE;
    private Button btnContinuar;

    private RestoreUserTask mRestoreUserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoreuser);

        ddlIdClub = (Spinner) findViewById(R.id.ddl_Club);
        txtNumMembresia = (EditText) findViewById(R.id.txtNumMembresia);
        txtCorreoE = (EditText) findViewById(R.id.txt_email);
        btnContinuar = (Button) findViewById(R.id.btn_continue);

        btnContinuar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue:
                loadRestauraCuenta();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operation");
        }
    }

    private void loadRestauraCuenta() {
        UserPojo pojo = new UserPojo();
        pojo.setmIdClub((int) ddlIdClub.getSelectedItemId());
        pojo.setmMemberNumber(Integer.parseInt(txtNumMembresia.getText().toString()));
        pojo.setmEmail(txtCorreoE.getText().toString());

        mRestoreUserTask = new RestoreUserTask(new ResponseInterface(){

            @Override
            public void onResultResponse(Object obj) {
                UserPojo userPojo = (UserPojo) obj;
            }
        });

        RestoreUserTask.mContext = this;
        mRestoreUserTask.execute(pojo);


        Toast.makeText(getApplicationContext(), "Actualiza passw ", Toast.LENGTH_LONG).show();
    }
}
