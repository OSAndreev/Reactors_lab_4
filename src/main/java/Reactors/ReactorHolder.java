package Reactors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReactorHolder {
    private ArrayList<Reactor> reactorList;
    public ReactorHolder(){this.reactorList = new ArrayList<>();
    }
    public void addReactor(Reactor reactor){
        reactorList.add(reactor);
    }
    public ArrayList<Reactor> getReactorList() {return reactorList;}

}