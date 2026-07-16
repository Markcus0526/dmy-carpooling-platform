//
//  AppMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"


@interface Drv_MainMgrViewController : SuperViewController <AccountSvcDelegate, BMKMapViewDelegate, OrderSvcDelegate, BMKLocationServiceDelegate, MainSvcDelegate,BMKGeoCodeSearchDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, UIPopoverControllerDelegate,UIAlertViewDelegate>
{
	/********************** UI Controls **********************/
	IBOutlet UILabel *                  _lblDescription;
	IBOutlet UIImageView *              _imgUser;
	__weak IBOutlet BMKMapView *        mMapView;
	BMKLocationService *                locService;
	BMKMapManager *        mapManager;
	NSTimer *                   mReportTimer;

	IBOutlet UIView *           _vwPersonVerify;
	IBOutlet UIView *           _vwDriverVerify;

	NSTimer *                   mSwitchVerifyTimer;


	UIImageView*						_imgBadge;

	int									anc_count;
	int									order_count;
	int									person_count;

	CLLocationManager  *locationManager;
    
    NSString *versionURL;
    __weak IBOutlet UIButton *tongchengBtn;
    __weak IBOutlet UIButton *chengtuBtn;
    
    long mSingleLastOrderID;
    
    NSString *startAddress;
    NSString *endAddress;
    NSMutableDictionary *orderDic;
    
    BOOL flageStartOrder;
    
    int flageOrder;;
}


//- (IBAction)onClickedSwitch:(id)sender;
//- (IBAction)onClickedNews:(id)sender;

- (IBAction)onClickedDriverVerify:(id)sender;
- (IBAction)onClickedPersonVerify:(id)sender;
- (IBAction)onClickedLongOrder:(id)sender;
- (IBAction)onClickedShortOrder:(id)sender;

@property (nonatomic, readwrite) CLLocationCoordinate2D mNowPos;

@end
