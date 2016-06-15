package client.View.gui;

import client.CachedData;
import client.View.ViewAlgorithms;
import core.Player;
import core.gamelogic.GraphsAlgorithms;
import core.gamelogic.actions.BuildEmpoKingAction;
import core.gamelogic.actions.PickTownBonusAction;
import core.gamemodel.PoliticsCard;
import core.gamemodel.RegionType;
import core.gamemodel.TownName;
import core.gamemodel.modelinterface.TownInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;

import java.util.*;

/**
 * Created by Matteo on 11/06/16.
 */
public class TownsWithBonusView implements HasMainAction{
    private volatile static TownsWithBonusView instance = null;

    private Map<TownName, TownView> townsView;
    private Map<TownName, ObjectImageView> townBonusView;
    private Map<TownView, EventHandler<MouseEvent>> eventsMap;
    private Map<ObjectImageView, EventHandler<MouseEvent>> bonusEventsMap;
    private ClassLoader classLoader;
    private Effect borderGlow;
    private ActionData action;
    private PopOver extraPopOver;
    private BooleanProperty resetProperty;

    public static TownsWithBonusView getInstance() {
        if (instance == null) {
            synchronized (ImagesMaps.class) {
                if (instance == null) {
                    instance = new TownsWithBonusView();
                }
            }
        }
        return instance;
    }

    private TownsWithBonusView() {
        classLoader = this.getClass().getClassLoader();
        townsView = new HashMap<>();
        townBonusView = new HashMap<>();
        eventsMap = new HashMap<>();
        bonusEventsMap = new HashMap<>();
        borderGlow = setShadowEffect();
        extraPopOver = new PopOver();
        resetProperty = new SimpleBooleanProperty(false);

        //When reached timeout all is resetted
        resetProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                removeAvailableHandlers();
                removeBonusHandlers();
                extraPopOver.hide();
            }
        });
        buildTownViews();
        buildTownBonusViews();
    }

    private void buildTownViews() {
        Image aImage = new Image(classLoader.getResourceAsStream("a.png"));
        Image bImage = new Image(classLoader.getResourceAsStream("b.png"));
        Image cImage = new Image(classLoader.getResourceAsStream("c.png"));
        Image dImage = new Image(classLoader.getResourceAsStream("d.png"));
        Image eImage = new Image(classLoader.getResourceAsStream("e.png"));
        Image fImage = new Image(classLoader.getResourceAsStream("f.png"));
        Image gImage = new Image(classLoader.getResourceAsStream("g.png"));
        Image hImage = new Image(classLoader.getResourceAsStream("h.png"));
        Image iImage = new Image(classLoader.getResourceAsStream("i.png"));
        Image jImage = new Image(classLoader.getResourceAsStream("j.png"));
        Image kImage = new Image(classLoader.getResourceAsStream("k.png"));
        Image lImage = new Image(classLoader.getResourceAsStream("l.png"));
        Image mImage = new Image(classLoader.getResourceAsStream("m.png"));
        Image nImage = new Image(classLoader.getResourceAsStream("n.png"));
        Image oImage = new Image(classLoader.getResourceAsStream("o.png"));
        townsView.put(TownName.A, new TownView(TownName.A, RegionType.SEA, 0.07257407407407407, 0.059109289617486336, 0.10459153122197, aImage));
        townsView.put(TownName.B, new TownView(TownName.B,RegionType.SEA, 0.061042592592592594, 0.24180327868852458, 0.114425925925926, bImage));
        townsView.put(TownName.C, new TownView(TownName.C,RegionType.SEA, 0.2155925925925926, 0.11958469945355191, 0.114583333333333, cImage));
        townsView.put(TownName.D, new TownView(TownName.D,RegionType.SEA, 0.2084852504710498, 0.2786885245901639, 0.105321313772738, dImage));
        townsView.put(TownName.E, new TownView(TownName.E,RegionType.SEA, 0.11026557621929187, 0.40846994535519127, 0.102691805475035, eImage));
        townsView.put(TownName.F, new TownView(TownName.F,RegionType.HILLS, 0.3811597344042335, 0.0868013698630137, 0.104449317367015, fImage));
        townsView.put(TownName.G, new TownView(TownName.G,RegionType.HILLS, 0.3948916963480114, 0.2467627118644068, 0.10285385614803205, gImage));
        townsView.put(TownName.H, new TownView(TownName.H,RegionType.HILLS, 0.40973005099866394, 0.3864406779661017, 0.09912460333496036, hImage));
        townsView.put(TownName.I, new TownView(TownName.I,RegionType.HILLS, 0.5466258390659746, 0.08813559322033898, 0.0967313203267906, iImage));
        townsView.put(TownName.J, new TownView(TownName.J,RegionType.HILLS, 0.5349027170062496, 0.2830508474576271, 0.0973074585507204, jImage));
        townsView.put(TownName.K, new TownView(TownName.K,RegionType.MOUNTAINS, 0.7117437356463746, 0.07401129943502825, 0.0999579670574619, kImage));
        townsView.put(TownName.L, new TownView(TownName.L,RegionType.MOUNTAINS, 0.684200387088455, 0.24884182660489743, 0.1078307727656072, lImage));
        townsView.put(TownName.M, new TownView(TownName.M,RegionType.MOUNTAINS, 0.6729745030117486, 0.416462482946794, 0.120203003974608, mImage));
        townsView.put(TownName.N, new TownView(TownName.N,RegionType.MOUNTAINS, 0.82539565232543, 0.16800354706684858, 0.113268215283765, nImage));
        townsView.put(TownName.O, new TownView(TownName.O,RegionType.MOUNTAINS, 0.829096739437645, 0.3542896174863388, 0.106006559623886, oImage));
    }

    private void buildTownBonusViews() {
        townBonusView.put(TownName.A, new ObjectImageView(null, 0.08028267559927046, 0.07499708049113233, 0.037));
        townBonusView.put(TownName.B, new ObjectImageView(null, 0.07129208879328723, 0.24587339699863574, 0.037));
        townBonusView.put(TownName.C, new ObjectImageView(null, 0.22129004564640675, 0.12388308321964529, 0.037));
        townBonusView.put(TownName.D, new ObjectImageView(null, 0.21219945884042354, 0.28812960436562074, 0.037));
        townBonusView.put(TownName.E, new ObjectImageView(null, 0.11973560962918661, 0.41191268758526605, 0.037));
        townBonusView.put(TownName.F, new ObjectImageView(null, 0.38903657045691414, 0.08858390177353342, 0.037));
        townBonusView.put(TownName.G, new ObjectImageView(null, 0.39681556103261644, 0.24956616643929058, 0.037));
        townBonusView.put(TownName.H, new ObjectImageView(null, 0.41330833087486385, 0.3880845839017735, 0.037));
        townBonusView.put(TownName.I, new ObjectImageView(null, 0.5485367103462978, 0.08994815825375171, 0.037));
        townBonusView.put(TownName.K, new ObjectImageView(null, 0.7146600426962434, 0.07976261937244201, 0.037));
        townBonusView.put(TownName.L, new ObjectImageView(null, 0.6872324841631532, 0.2598444747612551, 0.037));
        townBonusView.put(TownName.M, new ObjectImageView(null, 0.676830301126889, 0.4351050477489768, 0.037));
        townBonusView.put(TownName.N, new ObjectImageView(null, 0.8409536334768346, 0.17562482946793997, 0.037));
        townBonusView.put(TownName.O, new ObjectImageView(null, 0.8455768259373965, 0.35770668485675305, 0.037));
    }

    public Map<TownName, ObjectImageView> getTownBonusView() {
        return townBonusView;
    }

    public Map<TownName, TownView> getTownsView() {
        return townsView;
    }

    public static DropShadow setShadowEffect() {
        Glow glow = new Glow(0.8);
        DropShadow borderglow = new DropShadow();
        borderglow.setColor(Color.WHITE);
        borderglow.setWidth(70);
        borderglow.setHeight(70);
        borderglow.setInput(glow);
        borderglow.setBlurType(BlurType.GAUSSIAN);
        return borderglow;
    }

    //Build Emporium With King help handling
    public void changeTownListener(List<PoliticsCard> politicsSelected) {
        action = new ActionData();
        action.setSatisfyingCard(politicsSelected);
        TownName source = getKingPosition();
        action.setSource(source);

        int myCoins = CachedData.getInstance().getWealthPath().getPlayerPosition((Player)CachedData.getInstance().getMe());
        myCoins = myCoins - ViewAlgorithms.coinForSatisfaction(politicsSelected);
        if(myCoins<0) return;

        Map<TownName, Integer> availableTowns = new HashMap<>(GraphsAlgorithms.reachableTowns(CachedData.getInstance().getTowns(),source,myCoins));
        if (availableTowns.size()<=0) return;

        for(TownName name : townsView.keySet()) {
            TownView myTown = townsView.get(name);
            myTown.getTownPopOver().getContentNode().setDisable(true);

            if(availableTowns.containsKey(name) && myTown.areServantsAvailable() &&
                    CachedData.getInstance().getTown(name).hasEmporium((Player)CachedData.getInstance().getMe())) {
                EventHandler<MouseEvent> handler = setHandler(myTown, ViewAlgorithms.coinForSatisfaction(politicsSelected)+availableTowns.get(name));
                eventsMap.put(myTown,handler);

                myTown.setEffect(borderGlow);
                myTown.setOnMouseEntered(event -> myTown.setEffect(borderGlow));
                myTown.setOnMouseExited(event -> myTown.setEffect(borderGlow));

                myTown.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
            } else {
                myTown.setOnMouseEntered(event -> myTown.setEffect(null));
                myTown.setOnMouseExited(event -> myTown.setEffect(null));
            }
        }

        if(availableTowns.size()==0) removeAvailableHandlers();
    }

    private TownName getKingPosition() {
        for(TownInterface town : CachedData.getInstance().getTowns().values()) {
            if(town.isKingHere()) return town.getTownName();
        }
        throw new NoSuchElementException();
    }

    private EventHandler<MouseEvent> setHandler(TownView townView, int money) {
        return event -> {
            VBox vBox = new VBox(10);
            vBox.setPadding(new Insets(5));
            HBox container = new HBox(10);
            vBox.getChildren().add(new Label("Proceed? " + money + " coins needed"));

            Button no = new Button("No");
            no.setAlignment(Pos.CENTER);
            no.setOnMouseClicked(event1 -> removeAvailableHandlers());

            Button yes = new Button("Yes");
            yes.setAlignment(Pos.CENTER);
            yes.setOnMouseClicked(event1 -> {
                townView.getTownPopOver().hide();
                extraPopOver.hide();
                removeAvailableHandlers();
                BuildEmpoKingAction currentAction = new BuildEmpoKingAction(
                        (Player)CachedData.getInstance().getMe(),action.satisfyingCard,
                        getRegionFrom(townView.getTownName()),action.source,townView.getTownName(),money
                );
                CachedData.getInstance().getController().sendInfo(currentAction);
            });

            container.getChildren().addAll(yes,no);
            vBox.getChildren().add(container);
            extraPopOver.setContentNode(vBox);
            extraPopOver.show(townView, 20);
        };
    }

    private void removeAvailableHandlers() {
        eventsMap.keySet().forEach(element ->
            element.removeEventHandler(MouseEvent.MOUSE_CLICKED,eventsMap.get(element)));
        eventsMap.clear();
        townsView.keySet().forEach(element -> {
            TownView myTown = townsView.get(element);
            myTown.getTownPopOver().getContentNode().setDisable(false);
            myTown.setEffect(null);
            myTown.setOnMouseEntered(event -> myTown.setEffect(borderGlow));
            myTown.setOnMouseExited(event -> myTown.setEffect(null));
        });
    }

    //TownBonus Pick Action handling section
    public void changeBonusListener() {
        for(TownName name : townBonusView.keySet()) {
            ObjectImageView bonusView = townBonusView.get(name);
            EventHandler<MouseEvent> event = setbonusHandler(name);
            bonusEventsMap.put(bonusView,event);

            bonusView.setEffect(borderGlow);
            bonusView.addEventHandler(MouseEvent.MOUSE_CLICKED,event);
        }
    }

    private EventHandler<MouseEvent> setbonusHandler(TownName name) {
        return event -> {
            PickTownBonusAction action = new PickTownBonusAction((Player)CachedData.getInstance().getMe(),name);
            removeBonusHandlers();
            CachedData.getInstance().getController().sendInfo(action);
        };
    }

    private void removeBonusHandlers() {
        bonusEventsMap.keySet().forEach(element -> element.removeEventHandler(MouseEvent.MOUSE_CLICKED,bonusEventsMap.get(element)));
        bonusEventsMap.clear();
        townBonusView.values().forEach(view -> view.setEffect(null));
    }

    private RegionType getRegionFrom(TownName target) {
        if(target.ordinal()<5) return RegionType.SEA;
        else if(target.ordinal()<10) return RegionType.HILLS;
        else return RegionType.MOUNTAINS;
    }

    @Override
    public void setDisableBindingMainAction(BooleanProperty mainActionAvailable) {
        resetProperty.bind(mainActionAvailable.not());
    }

    private class ActionData {
        private RegionType regionType;
        private List<PoliticsCard> satisfyingCard;
        private TownName source;

        ActionData() {
            regionType = null;
            satisfyingCard = new ArrayList<>();
            source = null;
        }

        public void setRegionType(RegionType regionType) {
            this.regionType = regionType;
        }

        public void setSatisfyingCard(List<PoliticsCard> satisfyingCard) {
            this.satisfyingCard = satisfyingCard;
        }

        public void setSource(TownName source) {
            this.source = source;
        }
    }
}