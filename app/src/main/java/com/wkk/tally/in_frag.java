package com.wkk.tally;

import com.wkk.tally.db.DBManager;
import com.wkk.tally.db.TypeBean;

import java.util.ArrayList;
import java.util.List;

public class in_frag extends BaseRecordFragment{
    @Override
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        super.adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
        List<TypeBean> outList = DBManager.getTypeList(0);
        typeList.addAll(outList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveAcountToDB() {
        super.accountBean.setKind(0);
        DBManager.insertAccountBeanToTB(super.accountBean);
    }

}
