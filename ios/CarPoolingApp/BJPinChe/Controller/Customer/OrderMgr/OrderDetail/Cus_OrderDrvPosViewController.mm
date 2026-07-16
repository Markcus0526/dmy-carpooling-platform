//
//  Cus_OrderDrvPosViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/15/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_OrderDrvPosViewController.h"
@interface Cus_OrderDrvPosViewController ()

@end

@implementation Cus_OrderDrvPosViewController

@synthesize mOrderInfo;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initControls];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [_MapView viewWillAppear];
    _MapView.delegate = self;
    
    locService.delegate = self;
    [locService startUserLocationService];
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [_MapView viewWillDisappear];
    _MapView.delegate = nil;
    locService.delegate = nil;

}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
    mRouteSearch = [[BMKRouteSearch alloc] init];
    mRouteSearch.delegate = self;
    
    locService = [[BMKLocationService alloc]init];
    [locService startUserLocationService];

    [self callGetDriverPos];
    
    _MapView.showsUserLocation = NO;
    //模式设置
    _MapView.userTrackingMode = BMKUserTrackingModeNone;
    _MapView.showsUserLocation = YES;

}


- (void) drawDrvierPos:(NSString *)name lat:(double)lat lng:(double)lng
{
    BMKPointAnnotation* annotation = [[BMKPointAnnotation alloc]init];
	CLLocationCoordinate2D coor;
	coor.latitude = lat;
	coor.longitude = lng;
	annotation.coordinate = coor;
    
    annotation.title = name;
    
	[_MapView addAnnotation:annotation];
    
    BMKCoordinateRegion region = BMKCoordinateRegionMakeWithDistance(CLLocationCoordinate2DMake(lat, lng), 1500, 1500);
    [_MapView setRegion:region];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - BaiduMap Delegate

- (BMKAnnotationView *)mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>) annotation{
    if ([annotation isKindOfClass:[BMKPointAnnotation class]]) {
		BMKPinAnnotationView *newAnnotation = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"driver_pos"];
		newAnnotation.pinColor = BMKPinAnnotationColorPurple;
		newAnnotation.animatesDrop = YES;
		newAnnotation.draggable = NO;
        newAnnotation.image =[UIImage imageNamed:@"driverAnnotation"];
		
		return newAnnotation;
	}
	return nil;
}

////////////////////////////////////////////////////////////////////////////
#pragma mark - OrderSvcMgr Service Methods

- (void) callGetDriverPos
{
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDriverPos:[Common getUserID] driverid:mOrderInfo.driver_id DevToken:[Common getDeviceMacAddress]];
}

#pragma OrderSvcMgr Delegate Methods
- (void) getDriverPosResult:(NSString *)result driver_name:(NSString *)driver_name lat:(double)lat lng:(double)lng time:(NSString *)time
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        // update ui using detail info
        [self drawDrvierPos:driver_name lat:lat lng:lng];
        [locService startUserLocationService];

        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

////////////////////////////////////////////////////////////////////////////
#pragma mark - UI Control Event

- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}
#pragma mark 在地图View将要启动定位时，会调用此函数
- (void)mapViewWillStartLocatingUser:(BMKMapView *)mapView
{
    MyLog(@"start locate");
}
#pragma mark 用户位置更新后，会调用此函数
-(void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation
{
    MyLog(@"didUpdateUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    
    
    //地图更新显示
    [_MapView updateLocationData:userLocation];
    [locService stopUserLocationService];
}
- (void)didUpdateUserLocation:(BMKUserLocation *)userLocation
{
    
    
    
    
}

#pragma mark 在地图View停止定位后，会调用此函数
- (void)mapViewDidStopLocatingUser:(BMKMapView *)mapView
{
    MyLog(@"stop locate");
}
#pragma mark 定位失败后，会调用此函数
- (void)mapView:(BMKMapView *)mapView didFailToLocateUserWithError:(NSError *)error
{
    MyLog(@"location error");
}

@end
