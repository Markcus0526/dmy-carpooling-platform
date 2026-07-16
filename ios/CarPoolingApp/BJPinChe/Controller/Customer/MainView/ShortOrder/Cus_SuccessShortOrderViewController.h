//
//  Cus_SuccessShortOrderViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/11/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  乘客 单次拼车 出单成功界面

#import <UIKit/UIKit.h>

@interface Cus_SuccessShortOrderViewController : SuperViewController<OrderSvcDelegate>
{
	IBOutlet UIImageView *				_imgUser;
	IBOutlet UIImageView *				_imgSex;
	IBOutlet UILabel *					_lblAge;
	IBOutlet UILabel *					_lblName;

	IBOutlet UIImageView *				_imgCar;
	IBOutlet UILabel *					_carNo;
	IBOutlet UIImageView *				_imgCarType;
	IBOutlet UIImageView *				_imgCarBrand;
	IBOutlet UIImageView *				_imgCarSubType;
	IBOutlet UILabel *					_lblCarColor;

	IBOutlet UILabel *					_startToEndAddr;
	IBOutlet UILabel *					_lblDistance;
	IBOutlet UILabel *					_lblTime;
	IBOutlet UILabel *					_lblPassword;
    __weak IBOutlet UIScrollView *myscrollView;
}

@property (nonatomic, retain) NSString*						mPassword;
@property (nonatomic, retain) STDriverInfo*					mDriverInfo;
@property (nonatomic, retain) STSingleTimeOrderInfo*		mOrderInfo;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedMyOrder:(id)sender;

@end

