//
//  LongOrderMapviewViewController.m
//  BJPinChe
//
//  Created by yc on 14-12-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "LongOrderMapviewViewController.h"
@interface LongOrderMapviewViewController ()

@end

@implementation LongOrderMapviewViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    myMapview = [[BMKMapView alloc] initWithFrame:CGRectMake(0, 64, 320, self.view.frame.size.height-64)];
    
    myMapview.delegate = self;
    [self.view addSubview:myMapview];
    
    UIImage *image = [UIImage imageNamed:@"titlebar_back.png"];
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 320, 64)];
    imageView.image = image;
    [self.view addSubview:imageView];
    
    UIButton *imageBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [imageBtn setFrame:CGRectMake(10, 20, 25, 44)];
    [imageBtn setBackgroundImage:[UIImage imageNamed:@"btnBack.png"] forState:UIControlStateNormal];
    [imageView addSubview:imageBtn];
    
    UILabel *label =[[UILabel alloc]initWithFrame:CGRectMake(0, 20, 320, 44)];
    [label setBackgroundColor:[UIColor clearColor]];
    [label setText:@"地图"];
    [label setFont:[UIFont systemFontOfSize:20]];
    [label setTextColor:[UIColor whiteColor]];
    label.textAlignment = NSTextAlignmentCenter;
    [self.view addSubview:label];
    
    
    // Do any additional setup after loading the view from its nib.
    
    [self initMapView];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    myMapview.delegate = self;
    [myMapview setShowsUserLocation:YES];
}
-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [myMapview setShowsUserLocation:NO];
    myMapview.delegate = nil;
}
-(void)initMapView
{
    [myMapview setShowsUserLocation:YES];
    
    CLLocationCoordinate2D startCoor;
    startCoor.latitude = _longOrderDetailInfo.start_lat;
    startCoor.longitude = _longOrderDetailInfo.start_lng;
    
    CLLocationCoordinate2D endCoor;
    endCoor.latitude = _longOrderDetailInfo.end_lat;
    endCoor.longitude = _longOrderDetailInfo.end_lng;
    
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
    
    CGRect fitRect = [myMapview convertRegion:region toRectToView:myMapview];       //将地图视图的位置转换成地图的位置
    BMKMapRect fitMapRect = [myMapview convertRect:fitRect toMapRectFromView:myMapview];        //设置地图可视范围为数据所在的地图位置
    [myMapview setVisibleMapRect:fitMapRect animated:YES];
    myMapview.region = region;
    
    BMKPointAnnotation * startAnnotation = [[BMKPointAnnotation alloc]init];
    startAnnotation.coordinate = startCoor;
    startAnnotation.title = @"起点";
    startAnnotation.subtitle = [NSString stringWithFormat:@"%@ %@",_longOrderDetailInfo.start_city,_longOrderDetailInfo.start_addr];
    [myMapview addAnnotation:startAnnotation];
    
    BMKPointAnnotation * endAnntation = [[BMKPointAnnotation alloc]init];
    endAnntation.coordinate = endCoor;
    endAnntation.title = @"终点";
    endAnntation.subtitle = [NSString stringWithFormat:@"%@ %@",_longOrderDetailInfo.end_city,_longOrderDetailInfo.end_addr];
    [myMapview addAnnotation:endAnntation];
}

#pragma mark BMK -delegate
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
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
