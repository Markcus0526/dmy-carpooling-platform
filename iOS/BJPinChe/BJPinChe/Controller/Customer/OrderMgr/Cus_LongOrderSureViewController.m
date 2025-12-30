//
//  Cus_LongOrderSureViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//   长途拼车确认订单界面

#import "Cus_LongOrderSureViewController.h"
#import "LongWayOrderSvcMgr.h"
#import "UIImageView+WebCache.h"
#import "PasswordCheckModal.h"
#import "UIViewController+CWPopup.h"
#import "Cus_NotEnoughViewController.h"
#import "Cus_longOrderSuccessViewController.h"
#import "Cus_driverInfoViewController.h"
#import "BMKPointAnnotation.h"
#import "BMKPinAnnotationView.h"
#import "LongOrderMapviewViewController.h"

@interface Cus_LongOrderSureViewController ()

@end



@implementation Cus_LongOrderSureViewController
{
    NSMutableArray * _dateArray;
    NSMutableArray * _pickerArray;
    int _selectSeatCount;
    NSString * _password;
    LongWayOrderSvcMgr *longWayOrderSvcMgr;
}



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (void)loadView
{
    [super loadView];
    self.hidesBottomBarWhenPushed =YES;
}

- (void)viewDidLoad
{
	[super viewDidLoad];

	// Do any additional setup after loading the view.
	[self createDataSource];

	UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
	[rightBtn setBackgroundImage:[UIImage imageNamed:@"btn_refresh.png"] forState:UIControlStateNormal];
	[rightBtn setFrame:CGRectMake(0, 0, 30, 30)];
	[rightBtn addTarget:self action:@selector(upDateOrder) forControlEvents:UIControlEventTouchUpInside];
	UIBarButtonItem *backBar = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
	self.navigationItem.rightBarButtonItem = backBar;
}


-(void)upDateOrder
{
    [self createDataSource];
}


-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    UIScreen *screen = [UIScreen mainScreen];
    if (screen.bounds.size.height>500) {
        self.sureScrollView.contentSize = CGSizeMake(320, 560);
    }else
    {
        self.sureScrollView.contentSize = CGSizeMake(320, 650);
    }
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)createDataSource
{
	TEST_NETWORK_RETURN;
    longWayOrderSvcMgr = [[CommManager getCommMgr] longWayOrderSvcMgr];
    longWayOrderSvcMgr.delegate = self;
    [longWayOrderSvcMgr GetAcceptableLongOrderDetailInfoWithUserId:[Common getUserID] AndOrderId:[NSString stringWithFormat:@"%ld",self.orderId]];
    [SVProgressHUD show];
}

-(void)GetAcceptableLongOrderDetailInfo:(NSString *)result dataList:(NSMutableArray *)dataList
{
    //数据获取成功
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        _dateArray = [[NSMutableArray alloc]init];
        [_dateArray setArray:dataList];
        [self refreshView];
        
    }
    else  //数据获取失败
    {
        //_dateArray =nil;
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
        BACK_VIEW;
    }
    
}

-(void)viewWillAppear:(BOOL)animated
{
//    [_mapView viewWillAppear];
    self.mapView.delegate = self;
    self.mapView.showsUserLocation = YES;
}
-(void)viewWillDisappear:(BOOL)animated
{
//    [_mapView viewWillDisappear];
    self.mapView.delegate = nil;
}

-(void)refreshView
{
    STAcceptableLongOrderDetailInfo * longOrderDetailInfo = _dateArray[0];
    [self.driverImg setImageWithURL:[NSURL URLWithString:longOrderDetailInfo.driver_info.driver_img]];
    if (longOrderDetailInfo.driver_info.driver_gender == 0) {
        [self.genderImg setImage:[UIImage imageNamed:@"icon_female.png"]];
    }else{
        [self.genderImg setImage:[UIImage imageNamed:@"icon_male.png"]];
    }
    NSString * car_styleImageName = [NSString stringWithFormat:@"cartype%d_active.png",longOrderDetailInfo.driver_info.style];
    [self.car_style setImage:[UIImage imageNamed:car_styleImageName]];
    
    self.ageLabel.text = [NSString stringWithFormat:@"%d",longOrderDetailInfo.driver_info.driver_age];
    self.drv_career.text = [NSString stringWithFormat:@"驾龄 %d年",longOrderDetailInfo.driver_info.drv_career];
    self.evgood_rate.text = longOrderDetailInfo.driver_info.evgood_rate_desc;
    self.carpool_count.text = [NSString stringWithFormat:@"%d次",longOrderDetailInfo.driver_info.carpool_count];
    self.drv_name.text = longOrderDetailInfo.driver_info.driver_name;
    self.car_brand.text = longOrderDetailInfo.driver_info.brand;
    self.car_type.text = longOrderDetailInfo.driver_info.type;
    self.car_color.text = longOrderDetailInfo.driver_info.car_color;
    [self.driverImg setImageWithURL:[NSURL URLWithString:longOrderDetailInfo.driver_info.driver_img] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    
    
    [self.car_img setImageWithURL:[NSURL URLWithString:longOrderDetailInfo.driver_info.car_img] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
    
    self.start_city.text = longOrderDetailInfo.start_city;
    self.end_city.text = longOrderDetailInfo.end_city;
    self.start_addr.text = longOrderDetailInfo.start_addr;
    self.end_addr.text = longOrderDetailInfo.end_addr;
    
    self.start_addr.numberOfLines =0;
    [self.start_addr sizeToFit];
    
    self.end_addr.numberOfLines =0;
    [self.end_addr sizeToFit];
    
    self.start_datetime.text = [self dateFormatter:longOrderDetailInfo.start_time];
    self.seat_price.text = [NSString stringWithFormat:@"%.0f点/座",longOrderDetailInfo.price];

    self.leftSeatsCount.text = [NSString stringWithFormat:@"剩余%d座",  longOrderDetailInfo.left_seats];
    self.picker.dataSource = self;
    self.picker.delegate = self;
    _pickerArray = [[NSMutableArray alloc]init];
    _selectCountLabel.text = [NSString stringWithFormat:@"抢%d座 %.0f点", 1 , longOrderDetailInfo.price];
    for (int i = 0; i < longOrderDetailInfo.left_seats; i ++) {
        [_pickerArray addObject:[NSString stringWithFormat:@"抢%d座 %.0f点",i + 1 , (i + 1) * longOrderDetailInfo.price]];
    }
    [self.picker reloadAllComponents];
    
    
   
    [_mapView setShowsUserLocation:YES];
    
    CLLocationCoordinate2D startCoor;
    startCoor.latitude = longOrderDetailInfo.start_lat;
    startCoor.longitude = longOrderDetailInfo.start_lng;
    
    CLLocationCoordinate2D endCoor;
    endCoor.latitude = longOrderDetailInfo.end_lat;
    endCoor.longitude = longOrderDetailInfo.end_lng;
    
    BMKCoordinateRegion region;
    
    CLLocationCoordinate2D center;
    center.latitude = (startCoor.latitude + endCoor.latitude + 1)/2.0f;
    center.longitude = (startCoor.longitude + endCoor.longitude + 1)/2.0f;
    region.center = center;
    
    BMKCoordinateSpan span ;
    span.latitudeDelta = endCoor.latitude - startCoor.latitude > 0 ? endCoor.latitude - startCoor.latitude : startCoor.latitude - endCoor.latitude;
    span.latitudeDelta = span.latitudeDelta + 1;
    MyLog(@"%f",span.latitudeDelta);
    span.longitudeDelta = endCoor.longitude - startCoor.longitude > 0 ? endCoor.longitude - startCoor.longitude > 0 : startCoor.longitude - endCoor.longitude;
    span.longitudeDelta = span.longitudeDelta + 1;
    region.span = span;
    
    CGRect fitRect = [self.mapView convertRegion:region toRectToView:self.mapView];       //将地图视图的位置转换成地图的位置
    BMKMapRect fitMapRect = [self.mapView convertRect:fitRect toMapRectFromView:self.mapView];        //设置地图可视范围为数据所在的地图位置
    [self.mapView setVisibleMapRect:fitMapRect animated:YES];
    self.mapView.region = region;
    
    
    BMKPointAnnotation * startAnnotation = [[BMKPointAnnotation alloc]init];
    startAnnotation.coordinate = startCoor;
    startAnnotation.title = @"起点";
    startAnnotation.subtitle = [NSString stringWithFormat:@"%@ %@",longOrderDetailInfo.start_city,longOrderDetailInfo.start_addr];
    [_mapView addAnnotation:startAnnotation];

    BMKPointAnnotation * endAnntation = [[BMKPointAnnotation alloc]init];
    endAnntation.coordinate = endCoor;
    endAnntation.title = @"终点";
    endAnntation.subtitle = [NSString stringWithFormat:@"%@ %@",longOrderDetailInfo.end_city,longOrderDetailInfo.end_addr];
    [_mapView addAnnotation:endAnntation];

}


// 自定义大头针协议方法

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
        annotationView.pinColor = BMKPinAnnotationColorGreen;
        annotationView.image = [UIImage imageNamed:@"AnnoStartPoint.png"];
    }else{
        annotationView.pinColor = BMKPinAnnotationColorRed;
        annotationView.image = [UIImage imageNamed:@"AnnoEndPoint.png"];
    }
    
    // 设置主标题、副标题
    annotationView.canShowCallout = YES;
    // 设置下落动画
    annotationView.animatesDrop = YES;
    return annotationView;
}


#pragma mark 地图双击手势
- (void)mapview:(BMKMapView *)mapView onDoubleClick:(CLLocationCoordinate2D)coordinate
{
	[self toMapView];
}


- (void)toMapView {
	LongOrderMapviewViewController *lvc = [[LongOrderMapviewViewController alloc]init];
	lvc.longOrderDetailInfo = _dateArray[0];
	[self.navigationController pushViewController:lvc animated:YES];
}


-(NSString *)dateFormatter:(NSString *)dateString
{
    NSString * year = [dateString componentsSeparatedByString:@"-"][0];
    NSString * month = [dateString componentsSeparatedByString:@"-"][1];
    NSString * day = [([dateString componentsSeparatedByString:@"-"][2]) componentsSeparatedByString:@" "][0];
    NSString * hour = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][0];
    NSString * minute = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][1];
    
    return [NSString stringWithFormat:@"%@年%@月%@日 %@:%@",year,month,day,hour,minute];
}




- (IBAction)OnBackClick:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
    //BACK_VIEW;
}


- (IBAction)OnPickerCancelClick:(id)sender
{
    [self.pickerView setHidden:YES];
}
- (IBAction)OnPickerSureClick:(id)sender
{
    _selectSeatCount = [self.picker selectedRowInComponent:0];
    float price = [_dateArray[0] price];
    _selectCountLabel.text = [NSString stringWithFormat:@"抢%d座 %.0f点",_selectSeatCount + 1, (_selectSeatCount + 1) * price];
    [self.pickerView setHidden:YES];
}

- (IBAction)OnSelectSeatCountClick:(id)sender {
    [self.pickerView setHidden:NO];
}


-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
  
}



#pragma mark - uipikerview 协议实现
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [_pickerArray count];
}

-(NSString*) pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [_pickerArray objectAtIndex:row];
}


//点击“抢”按钮
- (IBAction)OnGrabClick:(id)sender
{
	TEST_NETWORK_RETURN;
   longWayOrderSvcMgr = [[CommManager getCommMgr] longWayOrderSvcMgr];
    longWayOrderSvcMgr.delegate = self;
    
    [longWayOrderSvcMgr AcceptLongOrderWithUserID:[Common getUserID] AndOrderId:self.orderId AndSeatsCount:_selectSeatCount + 1];
    [SVProgressHUD showWithStatus:@"抢座单正在提交，请稍候…" maskType:SVProgressHUDMaskTypeClear];
    
}
/**
 *  点击车主头像
 *
 *  @param sender <#sender description#>
 */
- (IBAction)OnDriverInfoClick:(id)sender {
    
    Cus_driverInfoViewController * driverInfoViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"DriverInfo"];
    
    STAcceptableLongOrderDetailInfo * longOrderDetailInfo = _dateArray[0];
    driverInfoViewController.driverid = longOrderDetailInfo.driver_info.driver_id;
    
    [self.navigationController pushViewController:driverInfoViewController animated:YES];
    //SHOW_VIEW(driverInfoViewController);
    
    
}
//点击“抢”按钮回调函数
- (void)AcceptLongOrder:(int)retcode retmsg:(NSString *)result dataList:(NSMutableArray *)dataList
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])			//抢座成功
	{
		[SVProgressHUD dismissWithSuccess:result];
		PasswordCheckModal *popup = [[PasswordCheckModal alloc] initWithNibName:@"PasswordCheckModal" bundle:nil];

		popup.delegate = self;

		[self presentPopupViewController:popup animated:YES completion:^(void) {
			MyLog(@"popup view presented");
		}];
	}
	else if (retcode == NotEnoughLvDian)			// 抢座失败
	{
		[SVProgressHUD dismissWithError:result];
		Cus_NotEnoughViewController * notEnoughView = [[Cus_NotEnoughViewController alloc]initWithNibName:@"Cus_NotEnoughViewController" bundle:nil];

		STGrabLongOrderFailInfo * failInfo = dataList[0];
		notEnoughView.leftPrice = [NSString stringWithFormat:@"%@",failInfo.rembal];
		notEnoughView.needPrice = [NSString stringWithFormat:@"%@",failInfo.total_fee];

		notEnoughView.delegate = self;

		[self presentPopupViewController:notEnoughView animated:YES completion:^(void) {
			MyLog(@"Cus_NotEnoughViewController's view presented");
		}];
	}
	else
	{
		[SVProgressHUD showErrorWithStatus:result];
	}
}


//点击绿点不足页面的按钮
-(void)Cus_notEnoughViewChargeOrNot:(BOOL)chargeOrNot
{
    if (chargeOrNot) {
        
        if (self.popupViewController != nil) {
            [self dismissPopupViewControllerAnimated:YES completion:^{
                MyLog(@"popup view dismissed");
                //跳转到充值页面
#warning 添加
                
                UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
                [self presentViewController:ctrl animated:NO completion:nil];
                
            }];
        }
        
        
    }else{
        //点击取消
        // dismiss popup
        if (self.popupViewController != nil) {
            [self dismissPopupViewControllerAnimated:YES completion:^{
                MyLog(@"popup view dismissed");
            }];
        }
        
    }
}


//输入密码点击确定时的操作
- (void)tappedDone:(NSString *)result
{
    _password = result;
    [longWayOrderSvcMgr SetLongOrderPasswordWithUserID:[Common getUserID] AndOrderId:self.orderId AndPassword:result];
    
    [SVProgressHUD show];
    
}

//设定长途订单密码回调函数
-(void)SetLongOrderPassword:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        // dismiss popup
        if (self.popupViewController != nil) {
            [self dismissPopupViewControllerAnimated:YES completion:^{
                MyLog(@"popup view dismissed");
                Cus_longOrderSuccessViewController * successViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderSuccess"];
                successViewController.successInfo = dataList[0];
                successViewController.successInfo.uid = self.orderId;
                
                SHOW_VIEW(successViewController);
                
            }];
        }
//        Cus_longOrderSuccessViewController * successViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderSuccess"];
//        successViewController.successInfo = dataList[0];
//        successViewController.successInfo.uid = [NSString stringWithFormat:@"%ld",self.orderId];
//        [self.navigationController pushViewController:successViewController animated:YES];
        
    }
    else
    {
        [SVProgressHUD dismissWithError:SVCERR_MSG_ERROR];
    }
    
}



- (IBAction)onClickCar:(id)sender {
	STAcceptableLongOrderDetailInfo * longOrderDetailInfo = _dateArray[0];


	UIView* mainView = [[UIView alloc] initWithFrame:[UIScreen mainScreen].bounds];
	mainView.backgroundColor = [UIColor blackColor];


	UIImageView* imgView = [[UIImageView alloc] initWithFrame:[UIScreen mainScreen].bounds];
	[imgView setImageWithURL:[NSURL URLWithString:longOrderDetailInfo.driver_info.car_img] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
	imgView.frame = CGRectMake(0, mainView.frame.size.height / 2 - mainView.frame.size.width / 2, mainView.frame.size.width, mainView.frame.size.width);


	UIButton* btnItem = [UIButton buttonWithType:UIButtonTypeCustom];
	[btnItem setBackgroundColor:[UIColor clearColor]];
	[btnItem addTarget:self action:@selector(onClickImage:) forControlEvents:UIControlEventTouchUpInside];
	btnItem.frame = mainView.bounds;


	[mainView addSubview:imgView];
	[mainView addSubview:btnItem];

	[self.navigationController.view addSubview:mainView];
}

- (void)onClickImage:(id)sender {
	UIButton* btnItem = (UIButton*)sender;
	UIView* viewMain = btnItem.superview;
	[viewMain performSelector:@selector(removeFromSuperview) withObject:nil afterDelay:0];
}


- (IBAction)onClickMap:(id)sender {
	[self toMapView];
}


@end
