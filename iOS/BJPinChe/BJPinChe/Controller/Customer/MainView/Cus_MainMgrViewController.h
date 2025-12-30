//
//  AppMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
// 乘客首页界面

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>

#import "BMapKit.h"

@interface Cus_MainMgrViewController : SuperViewController <AccountSvcDelegate, CLLocationManagerDelegate, BMKMapViewDelegate, BMKPoiSearchDelegate, BMKLocationServiceDelegate, BMKGeoCodeSearchDelegate, MainSvcDelegate,UIAlertViewDelegate>
{
	/********************** UI Controls **********************/
	IBOutlet UIView *					_vwValidate;
    
    __weak IBOutlet UIView *_vwValidate1;
    
    NSTimer *vwTimer;
    
	IBOutlet UILabel *					_lblDriverNum;

	UIImageView*							_imgBadge;
	// baidu map relation
	// BMKMapManager*					_mapManager;

	__weak IBOutlet BMKMapView *		cus_MapView;
	BMKLocationService *				locService;
//	BMKPoiSearch*						search;
	CLLocationCoordinate2D				myCoord;

	int									anc_count;
	int									order_count;
	int									person_count;
    
    
    CLLocationManager  *locationManager;
    
     NSString *versionURL;
}

//@property (nonatomic, strong) BMKMapView *      mapView;

//- (IBAction)onClickedSwitch:(id)sender;
//- (IBAction)onClickedNews:(id)sender;

- (IBAction)onClickedGPS:(id)sender;

- (IBAction)onClickedValidate:(id)sender;

- (IBAction)onClickedShortOrder:(id)sender;

@end
