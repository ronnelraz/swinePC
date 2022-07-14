// Generated code from Butter Knife. Do not modify!
package com.ronnelrazo.physical_counting;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Login_ViewBinding implements Unbinder {
  private Login target;

  @UiThread
  public Login_ViewBinding(Login target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Login_ViewBinding(Login target, View source) {
    this.target = target;

    target.str_permision = Utils.findRequiredViewAsType(source, R.id.permission, "field 'str_permision'", TextView.class);
    target.login = Utils.findRequiredViewAsType(source, R.id.login, "field 'login'", MaterialButton.class);
    target.version = Utils.findRequiredViewAsType(source, R.id.verison, "field 'version'", TextView.class);
    target.keepme = Utils.findRequiredViewAsType(source, R.id.keepme, "field 'keepme'", CheckBox.class);
    target.input = Utils.arrayFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.username, "field 'input'", TextInputEditText.class), 
        Utils.findRequiredViewAsType(source, R.id.password, "field 'input'", TextInputEditText.class));
    target.input_layout = Utils.arrayFilteringNull(
        Utils.findRequiredViewAsType(source, R.id.username_layout, "field 'input_layout'", TextInputLayout.class), 
        Utils.findRequiredViewAsType(source, R.id.password_layout, "field 'input_layout'", TextInputLayout.class));
  }

  @Override
  @CallSuper
  public void unbind() {
    Login target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.str_permision = null;
    target.login = null;
    target.version = null;
    target.keepme = null;
    target.input = null;
    target.input_layout = null;
  }
}
