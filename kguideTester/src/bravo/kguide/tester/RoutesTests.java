package bravo.kguide.tester;
import android.content.Context;
import android.test.AndroidTestCase;
import bravo.kguide.control.Controller;
import bravo.kguide.control.RouteList;
import bravo.kguide.control.Routes;

/**
 * @author 
 *
 * This class shall be run always upon change.
 * Tests should never fail
 * 
 * @coverage : RouteList.java , Routes.Java, Controller.Java
 */
public class RoutesTests extends AndroidTestCase {
	Controller controller;
	// TestCase route has id=8 , consisting of 5 coordinates, coords 1,3,5 have media while 2,4 don't	
	int routeExistsId=8,routeNotExistsId=999;
	int[] coordinateNoMedia={1,3};
	int[] coordinateMedia = {0,2,4};
	Context context=mContext;
	Routes routeExists, routeNotExists;
	RouteList routeList;
	
	public void setup(){
		controller = Controller.getInstance();
		routeExists = null;
		routeList = new RouteList();
	}
	
	/*
	public void setupRouteList(){
		routeList = new RouteList();
		routeList.addRoute(routeExists);
	}
	*/
	
	/**
	 * ##################
	 * Test routes class
	 * ##################
	 **/
	public void testGetExistingRoute(){
		routeExists = controller.getRoute(context, routeExistsId);

		if(routeExists != null)
			assertTrue((routeExists != null && routeExists.routeId == routeExistsId));
		else
			assertTrue(false);
	}

	/*
	public void testGetNonExistingRoute(){
		routeNotExists = controller.getRoute(context, routeNotExistsId);
	}
	
	public void testTextMessageAtCoordinate(){
		//
		//
		//
		this.setup();
	}
	
	public void testPhotoAtCoordinate(){
		this.setup();	
	}
	
	public void testLinkAtCoordinate(){
		this.setup();	
	}
	
	public void testAudioAtCoordinate(){
		this.setup();	
	}
	
	public void testAudioIsNotAtCoordinate(){
		this.setup();
	}
	
	public void testLinkIsNotAtCoordinate(){
		this.setup();
	}
	
	public void testPhotoIsNotAtCoordinate(){
		this.setup();
	}
	
	public void testTextIsNotAtCoordinate(){
		this.setup();
	}
	
	*/
	
	
	/**
	 * ##################
	 * Test routeList class
	 * ##################
	 **/
	/*
	public void testInsertExistingRouteInList(){
		routeExists = controller.getRoute(context, routeExistsId);
		routeList.addRoute(routeExists);
	}
	
	public void testInsertNonExistingRouteInList(){
		
	}

	public void testRouteIsInRouteList(){
		this.setup();
		controller.routeList.addRoute(routeExists);
		assertTrue(controller.routeList.routes.contains(routeExists));
	}
	
	public void testRouteNotInRouteList(){
		this.setup();
		assertFalse(controller.routeList.routes.contains(routeExists));
	}
	
	*/
}