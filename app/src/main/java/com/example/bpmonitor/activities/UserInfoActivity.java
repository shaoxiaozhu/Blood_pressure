public class UserInfoActivity extends AppCompatActivity {
    private EditText etAge, etHeight, etWeight, etGlucose;
    private RadioGroup rgGender;
    private Spinner spProvince, spEthnicity;
    private CheckBox cbHypertension, cbHyperlipidemia;
    private UserInfoViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        
        viewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        
        initViews();
        setupObservers();
        
        findViewById(R.id.btnSave).setOnClickListener(v -> saveUserInfo());
    }

    private void initViews() {
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etGlucose = findViewById(R.id.etGlucose);
        rgGender = findViewById(R.id.rgGender);
        spProvince = findViewById(R.id.spProvince);
        spEthnicity = findViewById(R.id.spEthnicity);
        cbHypertension = findViewById(R.id.cbHypertension);
        cbHyperlipidemia = findViewById(R.id.cbHyperlipidemia);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupObservers() {
        viewModel.getUserInfo().observe(this, userInfo -> {
            if (userInfo != null) {
                etAge.setText(String.valueOf(userInfo.getAge()));
                etHeight.setText(String.valueOf(userInfo.getHeight()));
                etWeight.setText(String.valueOf(userInfo.getWeight()));
                etGlucose.setText(String.valueOf(userInfo.getGlucose()));
                
                if ("男".equals(userInfo.getGender())) {
                    rgGender.check(R.id.rbMale);
                } else {
                    rgGender.check(R.id.rbFemale);
                }
                
                // 设置省份和民族选择
                setSpinnerSelection(spProvince, userInfo.getProvince());
                setSpinnerSelection(spEthnicity, userInfo.getEthnicity());
                
                cbHypertension.setChecked(userInfo.isHasHypertension());
                cbHyperlipidemia.setChecked(userInfo.isHasHyperlipidemia());
            }
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value != null) {
            for (int i = 0; i < spinner.getCount(); i++) {
                if (value.equals(spinner.getItemAtPosition(i).toString())) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveUserInfo() {
        UserInfo userInfo = new UserInfo();
        
        try {
            userInfo.setAge(Integer.parseInt(etAge.getText().toString()));
            userInfo.setHeight(Float.parseFloat(etHeight.getText().toString()));
            userInfo.setWeight(Float.parseFloat(etWeight.getText().toString()));
            userInfo.setGlucose(Float.parseFloat(etGlucose.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数值", Toast.LENGTH_SHORT).show();
            return;
        }
        
        userInfo.setGender(rgGender.getCheckedRadioButtonId() == R.id.rbMale ? "男" : "女");
        userInfo.setProvince(spProvince.getSelectedItem().toString());
        userInfo.setEthnicity(spEthnicity.getSelectedItem().toString());
        userInfo.setHasHypertension(cbHypertension.isChecked());
        userInfo.setHasHyperlipidemia(cbHyperlipidemia.isChecked());
        
        viewModel.saveUserInfo(userInfo);
        finish();
    }
}