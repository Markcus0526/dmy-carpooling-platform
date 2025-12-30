//
//  Drv_SuccessOrderViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_SuccessOrderViewController : SuperViewController<OrderSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    
    IBOutlet UILabel *                  _lblTime;
    IBOutlet UILabel *                  _lblAddress;

    __weak IBOutlet UIScrollView *myScrollView;

	STDetailedDrvOrderInfo*				order_detail_info;
}

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedGoOrder:(id)sender;


@property(atomic, readwrite) long		orderid;


@end
