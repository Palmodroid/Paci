package digitalgarden.permission;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import digitalgarden.R;

/**
 * Permission is essential for BestBoard to work
 * There are two possibilities:
 * - PermissionACTIVITY starts first, and it starts PrefsActivity, if perm.s are granted
 * - PermissionDIALOGFRAGMENT asks for permissions
 *
 * Good guide to create DialogFragments:
 * https://guides.codepath.com/android/Using-DialogFragment
 *
 */

public class PermissionRequestDialog extends DialogFragment
    {
    // Preference needed by checkStoragePermission()
    private String PERMISSION_ALREADY_REQUESTED = "permissionrequested";

    // only false to true changes should RELOAD bestboard
    // this should be RETAINED!!
    private boolean previousStoragePermissionCheck = true;

    private Button StoragePermissionButton;
    private Button StoragePermissionSettingsButton;
    private TextView StoragePermissionOk;

    OnPermissionRequestFinished onPermissionRequestFinished;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnPermissionRequestFinished
        {
        public void onPermissionRequestFinish(boolean permissionsGranted);
        }

    // Fragment needs an empty constructor - still don't understand why
    public PermissionRequestDialog() {super();}

    public static PermissionRequestDialog newInstance()
        {
        //Scribe.locus(Debug.PERMISSION);

        PermissionRequestDialog permissionRequestDialog = new PermissionRequestDialog();
        // needed to check preference change to true
        permissionRequestDialog.setRetainInstance( true );

        return permissionRequestDialog;
        }

    @Override
    public void onAttach(Activity context)
        {
        //Scribe.locus(Debug.PERMISSION);
        super.onAttach(context);

        try
            {
            onPermissionRequestFinished = (OnPermissionRequestFinished) context;
            }
        catch (ClassCastException e)
            {
            throw new ClassCastException( context.toString() + " must implement OnPermissionRequestFinished");
            }
        }


    @Override
    public void onDetach()
        {
        //Scribe.locus(Debug.PERMISSION);
        super.onDetach();

        onPermissionRequestFinished = null;
        }


    // http://code.google.com/p/android/issues/detail?id=17423
    // http://stackoverflow.com/questions/8235080/fragments-dialogfragment-and-screen-rotation
    // http://stackoverflow.com/questions/8417885/android-fragments-retaining-an-asynctask-during-screen-rotation-or-configuratio
    // http://stackoverflow.com/questions/3078389/java-default-no-argument-constructor
    @Override
    public void onDestroyView()
        {
        //Scribe.locus(Debug.PERMISSION);
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
        }


    @Override
    public void onCancel(DialogInterface dialog)
        {
        //Scribe.locus(Debug.PERMISSION);
        super.onCancel(dialog);

        // IF PERMISSIONS ARE GIVEN, FRAGMENT WILL DISMISS IN ONRESUME!
        onPermissionRequestFinished.onPermissionRequestFinish( false );
        }


    // onCreateView is called after onAttach
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        //Scribe.locus(Debug.PERMISSION);
        View view = inflater.inflate(R.layout.request_permission_dialog, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); // https://stackoverflow.com/a/15279400
        getDialog().setCanceledOnTouchOutside( false ); // https://stackoverflow.com/questions/16480114/dialogfragment-setcancelable-property-not-working

        /*
         * GRANTING STORAGE PERMISSION
         */

        // Asks permission for storage (external storage write access)
        // Stores that permission was already asked
        StoragePermissionButton = (Button) (view.findViewById(R.id.storage_permission_button));
        StoragePermissionButton.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                // Permission request code is not handled, permission is checked in the onResume method
                ActivityCompat.requestPermissions( getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1234);

                // Set PERMISSION_ALREADY_REQUESTED flag
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences( getActivity() );
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean(PERMISSION_ALREADY_REQUESTED, true );
                editor.apply();
                }
            });

        StoragePermissionSettingsButton = (Button) ( view.findViewById(R.id.storage_permission_settings_button));
        StoragePermissionSettingsButton.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                }
            });

        StoragePermissionOk = (TextView) (view.findViewById(R.id.storage_permission_ok));

        return view;
        }


    @Override
    public void onResume()
        {
        //Scribe.locus(Debug.PERMISSION);
        super.onResume();

        boolean storageEnabled = checkStoragePermission();

        // if both permissions are ready, we could finish
        if ( storageEnabled )
            {
            Toast.makeText(getActivity(), getString(R.string.permission_ready), Toast.LENGTH_SHORT).show();
            onPermissionRequestFinished.onPermissionRequestFinish( true );
            dismiss();
            }
        }


    public static boolean isStoragePermissionGranted(Context context )
        {
        return ContextCompat.checkSelfPermission( context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) ==
                PackageManager.PERMISSION_GRANTED;
        }


    /**
     * Check whether Storage Permisson is granted, and sets views according to the result
     * @return true if enabled, false if not
     */
    boolean checkStoragePermission()
        {
        if ( isStoragePermissionGranted( getActivity() ) )
            {
            //Scribe.debug(Debug.PERMISSION, "Storage permission is granted" );

            // if true, clear PERMISSION_ALREADY_REQUESTED flag
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences( getActivity() );
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean(PERMISSION_ALREADY_REQUESTED, false);
            editor.apply();

            // set views
            StoragePermissionOk.setVisibility(View.VISIBLE);
            StoragePermissionButton.setVisibility(View.GONE);
            StoragePermissionSettingsButton.setVisibility(View.GONE);

            if ( !previousStoragePermissionCheck) // false -> true change
                {
                //Scribe.debug(Debug.PERMISSION, "Storage permission is changed to ENABLED, BestBoard should reload. (if already started)" );
                //PrefsFragment.performAction( getActivity(), PrefsFragment.PREFS_ACTION_RELOAD);
                }
            previousStoragePermissionCheck = true;
            }
        else
            {
            //Scribe.debug(Debug.PERMISSION, "Storage permission is NOT granted" );
            StoragePermissionOk.setVisibility(View.GONE);

            // should NOT show rationale:
            //      - First run
            //      - User do not want to grant permission -> ONLY SETTINGS WORK
            // should show rationale:
            //      - Consequent run, user is not decided yet
            boolean shouldShowRequestPermissionRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale( getActivity(),
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            // read PERMISSION_ALREADY_REQUESTED flag
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences( getActivity() );
            boolean permissionRequested = sharedPrefs.getBoolean(PERMISSION_ALREADY_REQUESTED, false );

            if ( !permissionRequested ||                    // not yet requested, first run
                    shouldShowRequestPermissionRationale )  // requested, but not decided (info already shown)
                {
                StoragePermissionButton.setVisibility(View.VISIBLE);
                StoragePermissionSettingsButton.setVisibility(View.GONE);
                }
            else                                            // requested, but not granted; AND no info wanted
                {
                StoragePermissionButton.setVisibility(View.GONE);
                StoragePermissionSettingsButton.setVisibility(View.VISIBLE);
                }

            previousStoragePermissionCheck = false;
            }
        // this is the current one
        return previousStoragePermissionCheck;
        }

    }
