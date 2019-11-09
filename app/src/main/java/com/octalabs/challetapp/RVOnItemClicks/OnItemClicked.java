package com.octalabs.challetapp.RVOnItemClicks;

public interface OnItemClicked<T> {
    void onClick(int pos,T obj);
    void onClick(boolean isChecked,int pos,T obj);


}
