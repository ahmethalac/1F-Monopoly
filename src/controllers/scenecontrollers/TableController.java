package controllers.scenecontrollers;

import controllers.modelcontrollers.GameManager;
import controllers.observers.BuildingObserver;
import controllers.observers.ColorObserver;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import models.*;
import storage.filemanager.MeshImporter;
import storage.filemanager.SettingImporter;
import utils.RegionList;

import java.util.ArrayList;
import java.util.List;

public class TableController extends SubScene {
    private GameSceneController gameSceneController;
    private final Group sceneItems;
    private ArrayList<RegionList> regionsList = new ArrayList<>();

    public TableController(GameSceneController gameSceneController) {
        super(new Group(),
                SceneManager.getInstance().getWidth(),
                SceneManager.getInstance().getHeight(),
                true,
                SceneAntialiasing.DISABLED);
        this.gameSceneController = gameSceneController;
        this.setFill(Color.SILVER);
        sceneItems = (Group) this.getRoot();

        addCamera();
        addLight();

        sceneItems.getChildren().addAll(MeshImporter.getTable());

        initializeRegions();

        //Experimental
        MeshView[] astronaut = MeshImporter.getPlayer();
        for ( MeshView node : astronaut){
            node.translateZProperty().set(-100);
            node.translateYProperty().set(-800);
        }
        sceneItems.getChildren().addAll(astronaut);
    }

    public void rotateTable(){
        rotateAroundCenter(this.getCamera(), 90);
        //Experimental
        ((City)GameManager.getInstance().getRegions().get(1)).setOwner(new Player("ahmet","red","a",2));
        ((City)GameManager.getInstance().getRegions().get(2)).setOwner(new Player("ahmet","blue","a",2));
        ((City)GameManager.getInstance().getRegions().get(3)).setOwner(new Player("ahmet","pink","a",2));
        ((City)GameManager.getInstance().getRegions().get(4)).setOwner(new Player("ahmet","green","a",2));
        ((City)GameManager.getInstance().getRegions().get(5)).setOwner(new Player("ahmet","yellow","a",2));
        ((City)GameManager.getInstance().getRegions().get(6)).setOwner(new Player("ahmet","orange","a",2));
        ((City)GameManager.getInstance().getRegions().get(7)).setOwner(new Player("ahmet","purple","a",2));
        ((City)GameManager.getInstance().getRegions().get(8)).setOwner(new Player("ahmet","cyan","a",2));
    }

    private void initializeRegions() {
        ArrayList<Region> regions = GameManager.getInstance().getRegions();
        List<int[]> coordinates = SettingImporter.getRegionCoordinates();

        for ( int i = 0; i < coordinates.size(); i++){
            MeshView region = MeshImporter.getRegion();
            region.translateXProperty().set(coordinates.get(i)[0]);
            region.translateYProperty().set(coordinates.get(i)[1]);
            region.translateZProperty().set(0);

            int finalI = i;

            RegionList group = new RegionList(region, sceneItems, regions.get(i), mouseEvent -> {
                if ( regions.get(finalI) instanceof City){
                    gameSceneController.handleCityPopup((City) regions.get(finalI));
                } else{
                    //Debug Purposes
                    System.out.println(regions.get(finalI).getClass().getName());
                }
            });
            regionsList.add(group);

            if ( regions.get(i) instanceof City){
                new ColorObserver(regions.get(i), group);
                new BuildingObserver(regions.get(i), group);
                region.setMaterial(new PhongMaterial(Color.GREY));
            } else if ( regions.get(i) instanceof ChanceRegion){
                region.setMaterial(new PhongMaterial(Color.GREEN));
            } else if ( regions.get(i) instanceof PirateRegion){
                region.setMaterial(new PhongMaterial(Color.RED));
            } else if ( regions.get(i) instanceof StartingRegion){
                region.setMaterial(new PhongMaterial(Color.WHITE));
            }

        }
    }

    private void addLight() {
        LightBase ambient = new AmbientLight();
        ambient.translateZProperty().set(-1000);
        sceneItems.getChildren().add(ambient);
    }

    private void addCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.translateZProperty().set(-900);
        camera.translateYProperty().set(1300);
        camera.getTransforms().add(new Rotate(50, Rotate.X_AXIS));
        camera.setNearClip(1);
        camera.setFarClip(3000);
        this.setCamera(camera);
    }

    private void rotateAroundCenter(Node node, double angle){
        double x = node.getTranslateX();
        double y = node.getTranslateY();
        double rotate = node.getRotate();
        System.out.println(x);
        System.out.println(y);
        Timeline timeline2 = new Timeline(
                new KeyFrame(Duration.seconds(angle / 60), new KeyValue(node.translateYProperty(), x * Math.sin(angle * Math.PI / 180) + y * Math.cos(angle * Math.PI / 180)))
        );
        timeline2.play();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(angle / 60), new KeyValue(node.translateXProperty(), x * Math.cos(angle * Math.PI / 180) - y * Math.sin(angle * Math.PI / 180)))
        );
        timeline.play();

        Timeline timeline3 = new Timeline(
                new KeyFrame(Duration.seconds(angle / 60), new KeyValue(node.rotateProperty(), rotate + angle))
        );
        timeline3.play();
    }
}
