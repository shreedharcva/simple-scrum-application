
package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.AboutFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AboutActivity extends BaseSinglePaneActivity {

    @Override
    protected Fragment onCreatePane() {
        return new AboutFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }
}
