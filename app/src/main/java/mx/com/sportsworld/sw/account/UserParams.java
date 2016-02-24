package mx.com.sportsworld.sw.account;

/**
 * Created by dave on 22/02/2016.
 */
public class UserParams {

    public int getIdMember() {
        return mIdMember;
    }

    public void setIdMember(int idMember) {
        mIdMember = idMember;
    }

    public int getNumIntegrantes() {
        return mNumIntegrantes;
    }

    public void setNumIntegrantes(int numIntegrantes) {
        mNumIntegrantes = numIntegrantes;
    }

    public String getCorreoE() {
        return mCorreoE;
    }

    public void setCorreoE(String correoE) {
        mCorreoE = correoE;
    }

    public String getConfCorreoE() {
        return mConfCorreoE;
    }

    public void setConfCorreoE(String confCorreoE) {
        mConfCorreoE = confCorreoE;
    }

    public String getUsuario() {
        return mUsuario;
    }

    public void setUsuario(String usuario) {
        mUsuario = usuario;
    }

    public String getPassw() {
        return mPassw;
    }

    public void setPassw(String passw) {
        mPassw = passw;
    }

    public String getConfPassw() {
        return mConfPassw;
    }

    public void setConfPassw(String confPassw) {
        mConfPassw = confPassw;
    }

    public long getIdClub() {
        return midClub;
    }

    public long getTipoMember() {
        return mTipoMember;
    }

    public void setTipoMember(int tipoMember) {
        mTipoMember = tipoMember;
    }

    public void setIdClub(long midClub) {
        this.midClub = midClub;
    }

    public void setTipoMember(long tipoMember) {
        mTipoMember = tipoMember;
    }

    private long midClub;

    private long mTipoMember;

    private int mIdMember;

    private int mNumIntegrantes;

    private String mCorreoE;

    private String mConfCorreoE;

    private String mUsuario;

    private String mPassw;

    private String mConfPassw;

}
