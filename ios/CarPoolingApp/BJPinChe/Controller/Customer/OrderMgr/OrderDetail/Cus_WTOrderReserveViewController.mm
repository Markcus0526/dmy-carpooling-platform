//
//  Cus_WTOrderReserveViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_WTOrderReserveViewController.h"

@interface Cus_WTOrderReserveViewController ()

@end

@implementation Cus_WTOrderReserveViewController

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
}
-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [_MapView viewWillDisappear];
    _MapView.delegate = nil;
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
    
    [self callGetDetailOrderInfo];
}


/**
 * Update ui using detailed info
 */
- (void) updateUI
{
    // set detail information
    [_imgUser setImageWithURL:[NSURL URLWithString:mDetOrderInfo.driver_info.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    if(mDetOrderInfo.driver_info.sex == SEX_MALE) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = [UIColor orangeColor];
    }
    [_imgCar setImageWithURL:[NSURL URLWithString:mDetOrderInfo.driver_info.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
    
    [_lblAge setText:[NSString stringWithFormat:@"%d", mDetOrderInfo.driver_info.age]];
    [_lblName setText:mDetOrderInfo.driver_info.name];
    [_lblEvalPro setText:mDetOrderInfo.driver_info.goodeval_rate_desc];
    [_lblServeCnt setText:mDetOrderInfo.driver_info.carpool_count_desc];
    [_lblStartPos setText:mDetOrderInfo.start_addr];
    [_lblEndPos setText:mDetOrderInfo.end_addr];
    [_lblOrderTime setText:mDetOrderInfo.create_time];
    NSString *strMidPoints = @"";
    for(STMidPoint *midPoint in mDetOrderInfo.mid_points)
    {
        if([strMidPoints length] <= 0)
        {
            strMidPoints = [midPoint address];
        }
        else
        {
            strMidPoints = [NSString stringWithFormat:@"%@, %@", strMidPoints, [midPoint address]];
        }
    }
    [_lblMidPoints setText:strMidPoints];
    
    
    // show valid days
    NSArray * selDays = [mDetOrderInfo.valid_days componentsSeparatedByString:@","];
    if ([selDays count] > 0) {
        for (int i = 0; i < selDays.count; i++) {
            int dayNum = [selDays[i] intValue];
            UIButton * button = (UIButton *)[self.view viewWithTag:(100 + dayNum)];
            [button setEnabled:YES];
            [button setSelected:YES];
        }
    }
    // show selected days
    selDays = [mDetOrderInfo.left_days componentsSeparatedByString:@","];
    if ([selDays count] > 0) {
        for (int i = 0; i < selDays.count; i++) {
            int dayNum = [selDays[i] intValue];
            UIButton * button = (UIButton *)[self.view viewWithTag:(100 + dayNum)];
            [button setEnabled:YES];
        }
    }
    
    // make position data
    CLLocationCoordinate2D startPos;
    CLLocationCoordinate2D endPos;
    startPos.latitude = mDetOrderInfo.start_lat;
    startPos.longitude = mDetOrderInfo.start_lng;
    endPos.latitude = mDetOrderInfo.end_lat;
    endPos.longitude = mDetOrderInfo.end_lng;
    
    // draw drive route on map
    [self drawDriveRoute:startPos endPos:endPos];
}


-(void) drawDriveRoute : (CLLocationCoordinate2D)startPos endPos:(CLLocationCoordinate2D)endPos

{
    NSArray* array = [NSArray arrayWithArray:_MapView.annotations];
    [_MapView removeAnnotations:array];
    
    array = [NSArray arrayWithArray:_MapView.overlays];
    [_MapView removeOverlays:array];
    
    
    // show start & end position in sight
    CLLocationCoordinate2D startCoor = startPos;
    CLLocationCoordinate2D endCoor = endPos;
    
    BMKCoordinateRegion region;
    
    CLLocationCoordinate2D center;
    center.latitude = (startCoor.latitude + endCoor.latitude)/2.0f;
    center.longitude = (startCoor.longitude + endCoor.longitude)/2.0f;
    region.center = center;
    
    BMKCoordinateSpan span ;
    span.latitudeDelta = endCoor.latitude - startCoor.latitude > 0 ? endCoor.latitude - startCoor.latitude : startCoor.latitude - endCoor.latitude;
    span.latitudeDelta = span.latitudeDelta;
    NSLog(@"%f",span.latitudeDelta);
    span.longitudeDelta = endCoor.longitude - startCoor.longitude > 0 ? endCoor.longitude - startCoor.longitude > 0 : startCoor.longitude - endCoor.longitude;
    span.longitudeDelta = span.longitudeDelta;
    region.span = span;
    
    CGRect fitRect = [_MapView convertRegion:region toRectToView:_MapView];       //将地图视图的位置转换成地图的位置
    
    BMKMapRect fitMapRect = [_MapView convertRect:fitRect toMapRectFromView:_MapView];        //设置地图可视范围为数据所在的地图位置
    [_MapView setVisibleMapRect:fitMapRect animated:YES];
    
    // draw drive route on map
    BMKDrivingRoutePlanOption * planOption = [[BMKDrivingRoutePlanOption alloc] init];
    BMKPlanNode * start = [[BMKPlanNode alloc] init];
    BMKPlanNode * end = [[BMKPlanNode alloc] init];
    
    start.pt = startPos;
    end.pt = endPos;
    
    planOption.from = start;
    planOption.to = end;
    
    [mRouteSearch drivingSearch:planOption];
}

//////////////////////////////////////////////////////////////////////
#pragma mark - BaiduMap Delegate

/**
 * Get driving route delegate
 */
-(void) onGetDrivingRouteResult:(BMKRouteSearch *)searcher result:(BMKDrivingRouteResult *)result errorCode:(BMKSearchErrorCode)error
{
    NSLog(@"%d", error);
    
    if (error == BMK_SEARCH_NO_ERROR) {
        
        BMKDrivingRouteLine * plan = [result.routes objectAtIndex:0];
        int size = plan.steps.count;
        
        CLLocationCoordinate2D polylineCoords[size];
        for (int i = 0; i < size; i++) {
            BMKDrivingStep * drivingStep = [plan.steps objectAtIndex:i];
            // add annotation
            BMKPointAnnotation * item = [[BMKPointAnnotation alloc] init];
            item.coordinate = drivingStep.entrace.location;
            polylineCoords[i] = drivingStep.entrace.location;
            item.title = drivingStep.entraceInstruction;
            
            // add start position
            if (i == 0) {
                item.title = @"起点";
                [_MapView addAnnotation:item];
            }
            // add end position
            if (i == (size - 1)) {
                item.title = @"终点";
                [_MapView addAnnotation:item];
            }
        }
        
        // draw line
        BMKPolyline * polyLine = [BMKPolyline polylineWithCoordinates:polylineCoords count:size];
        [_MapView addOverlay:polyLine];
        
    }
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
    
    // 设置颜色
    if ([annotation.title isEqualToString:@"起点"]) {
        annotationView.image = [UIImage imageNamed:@"AnnoStartPoint.png"];
    }else{
        annotationView.image = [UIImage imageNamed:@"AnnoEndPoint.png"];
    }
    // 设置主标题、副标题
    annotationView.canShowCallout = YES;
    // 设置下落动画
    annotationView.animatesDrop = YES;
    return annotationView;
}



- (BMKOverlayView *)mapView:(BMKMapView *)mapView viewForOverlay:(id <BMKOverlay>)overlay
{
    if ([overlay isKindOfClass:[BMKPolyline class]]) {
        BMKPolylineView * polylineView = [[BMKPolylineView alloc] initWithOverlay:overlay];
        polylineView.fillColor = [UIColor blackColor];
        polylineView.strokeColor = [UIColor blueColor];
        polylineView.lineWidth = 6.0f;
        
        return polylineView;
    }
    
    return nil;
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

- (IBAction)onClickedReserve:(id)sender
{
    
}

////////////////////////////////////////////////////////////////////////////
#pragma mark - OrderSvcMgr Service Methods

- (void) callGetDetailOrderInfo
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDetailedCustomerOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}


#pragma OrderSvcMgr Delegate Methods
- (void) getDetailedCustomerOrderInfoResult:(NSString *)result OrderInfo:(STDetailedCusOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        mDetOrderInfo = order_info;
        
        // update ui using detail info
        [self updateUI];
        
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

@end
