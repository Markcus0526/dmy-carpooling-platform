//
//  FindDriverViewController.m
//  BJPinChe
//
//  Created by CKK on 14-9-16.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  查看车主位置界面  

#import "FindDriverViewController.h"
#import "BMKPointAnnotation.h"
#import "BMKPinAnnotationView.h"
@interface FindDriverViewController ()

@end

@implementation FindDriverViewController
{
    STdriverPosInfo * _driverPosInfo;
//    BMKMapView * BmapView;
}
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
    [self getdriverPos];
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated
{
//    BmapView = [[BMKMapView alloc]initWithFrame:self.mapView.bounds];
    _mapView.delegate = self;
    [_mapView setShowsUserLocation:YES];
    _mapView.zoomLevel = 14;
//    [self.mapView addSubview:BmapView];
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [_mapView setShowsUserLocation:NO];
    _mapView.delegate = nil;
    _mapView.zoomEnabled = YES;
}
-(void)getdriverPos
{
    LongWayOrderSvcMgr * longWayOrderSvcMgr = [[CommManager getCommMgr] longWayOrderSvcMgr];
    longWayOrderSvcMgr.delegate = self;
    [longWayOrderSvcMgr GetDriverPosWithUserID:[Common getUserID] AndDriverId:self.driverId];
}

-(void)GetDriverPos:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        _driverPosInfo = dataList[0];
        [self refreshView];
    }else{
        [SVProgressHUD show];
        [SVProgressHUD dismissWithError:GET_DRIVER_POS_ERROR afterDelay:DEF_DELAY];
    }
}

-(void)refreshView
{
    CLLocationCoordinate2D coor;
    coor.latitude = _driverPosInfo.lat;
    coor.longitude = _driverPosInfo.lng;

    [_mapView setCenterCoordinate:coor];
    
    BMKPointAnnotation * pointAnnotation = [[BMKPointAnnotation alloc]init];
    pointAnnotation.title = _driverPosInfo.driver_name;
    pointAnnotation.coordinate = coor;
    [_mapView addAnnotation:pointAnnotation];
}

-(BMKAnnotationView *)mapView:(BMKMapView *)mapView viewForAnnotation:(id<BMKAnnotation>)annotation
{
    static NSString *annotationName = @"annotation";
    
    // 皮肤
    BMKPinAnnotationView *annotationView = (BMKPinAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:annotationName];
    if(annotationView == nil)
    {
        annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:annotationName];
    }
    annotationView.pinColor = BMKPinAnnotationColorGreen;
    annotationView.image = [UIImage imageNamed:@"driverAnnotation"];
    
    // 设置主标题、副标题显示
    annotationView.canShowCallout = YES;
    // 设置下落动画
    annotationView.animatesDrop = YES;
    
    return annotationView;

}
- (IBAction)OnBackClick:(id)sender {
    BACK_VIEW;
}
@end
