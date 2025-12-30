//
//  Cus_LongOrderSureViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMKMapView.h"
#import "PasswordCheckModal.h"
#import "Cus_NotEnoughViewController.h"
@interface Cus_LongOrderSureViewController : UIViewController<LongWayOrderSvcDelegate,UIPickerViewDataSource,UIPickerViewDelegate,pwdPopupDelegate,Cus_NotEnoughViewDelegate,BMKMapViewDelegate>
@property (nonatomic) long orderId;
- (IBAction)OnBackClick:(id)sender;

@property (weak, nonatomic) IBOutlet UIView *pickerView;
@property (weak, nonatomic) IBOutlet UIPickerView *picker;
@property (weak, nonatomic) IBOutlet UILabel *selectCountLabel;
- (IBAction)OnPickerCancelClick:(id)sender;
- (IBAction)OnPickerSureClick:(id)sender;
- (IBAction)OnSelectSeatCountClick:(id)sender;
@property (weak, nonatomic) IBOutlet UIScrollView *sureScrollView;

//pickerView数据源
@property (strong, nonatomic) NSMutableArray * pickerArray;


//页面控件
@property (weak, nonatomic) IBOutlet UIImageView *genderImg;
@property (weak, nonatomic) IBOutlet UILabel *ageLabel;
@property (weak, nonatomic) IBOutlet UIImageView *driverImg;
@property (weak, nonatomic) IBOutlet UILabel *drv_career;
@property (weak, nonatomic) IBOutlet UIImageView *car_img;
@property (weak, nonatomic) IBOutlet UILabel *evgood_rate;
@property (weak, nonatomic) IBOutlet UILabel *carpool_count;
@property (weak, nonatomic) IBOutlet UILabel *drv_name;
@property (weak, nonatomic) IBOutlet UILabel *car_type;
@property (weak, nonatomic) IBOutlet UIImageView *car_style;
@property (weak, nonatomic) IBOutlet UILabel *car_brand;
@property (weak, nonatomic) IBOutlet UILabel *car_color;
@property (weak, nonatomic) IBOutlet UILabel *start_city;
@property (weak, nonatomic) IBOutlet UILabel *end_city;
@property (weak, nonatomic) IBOutlet UILabel *start_addr;
@property (weak, nonatomic) IBOutlet UILabel *end_addr;
@property (weak, nonatomic) IBOutlet UILabel *start_datetime;
@property (weak, nonatomic) IBOutlet UILabel *seat_price;
@property (weak, nonatomic) IBOutlet UILabel *leftSeatsCount;
@property (weak, nonatomic) IBOutlet BMKMapView *mapView;
- (IBAction)OnGrabClick:(id)sender;
- (IBAction)OnDriverInfoClick:(id)sender;


@end
