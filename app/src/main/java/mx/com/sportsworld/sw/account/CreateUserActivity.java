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

public class CreateUserActivity extends Activity implements View.OnClickListener {

    private Button btnCrearCuenta;

    private Spinner ddlIdClub;

    private Spinner ddlTipoMember;

    private EditText txtNumMembresia;

    private EditText txtNumIntegrantes;

    private EditText txtCorreoE;

    private EditText txtConfCorreoE;

    private EditText txtUsuario;

    private EditText txtPassw;

    private EditText txtConfPassw;

    private CreateUserTask mCreateUserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        ddlIdClub = (Spinner) findViewById(R.id.ddl_Club);

        ddlTipoMember = (Spinner) findViewById(R.id.ddl_TipoMembresia);

        txtNumMembresia = (EditText) findViewById(R.id.txtNumMembresia);

        txtNumIntegrantes = (EditText) findViewById(R.id.txtNumIntegrantes);

        txtCorreoE = (EditText) findViewById(R.id.txtCorreoE);

        txtConfCorreoE = (EditText) findViewById(R.id.txtConfCorreoE);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);

        txtPassw = (EditText) findViewById(R.id.txtPassw);

        txtConfPassw = (EditText) findViewById(R.id.txtConfPassw);

        btnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(this);

    }

    private void loadCrearCuenta() {

        UserPojo pojo = new UserPojo();
        pojo.setmIdClub((int) ddlIdClub.getSelectedItemId());
        pojo.setmMemberType(String.valueOf(ddlTipoMember.getSelectedItemId()));
        pojo.setmMemberNumber(Integer.parseInt(txtNumMembresia.getText().toString()));
        pojo.setNumeroIntegrantes(Integer.parseInt(txtNumIntegrantes.getText().toString()));
        pojo.setmEmail(txtCorreoE.getText().toString());
        pojo.setEmailConfirm(txtConfCorreoE.getText().toString());
        pojo.setUsername(txtUsuario.getText().toString());
        pojo.setPassword(txtPassw.getText().toString());
        pojo.setPasswordConfirm(txtConfPassw.getText().toString());

        mCreateUserTask = new CreateUserTask(new ResponseInterface(){

            @Override
            public void onResultResponse(Object obj) {
                UserPojo userPojo = (UserPojo) obj;
                //showOnLoggingProgressBar(false, true);
            }
        });

        CreateUserTask.mContext = this;
        mCreateUserTask.execute(pojo);

        Toast.makeText(getApplicationContext(), "carga cuenta" + " ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCrearCuenta:
                loadCrearCuenta();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operation");
        }
    }
}
