//
//  NewsMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/23/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  司机身份 我的订单界面

#import <UIKit/UIKit.h>
#import "Drv_OrderSingleCell.h"
#import "Drv_OrderMultiCell.h"
#import "Drv_EvalCustomerViewController.h"

@interface Drv_OrderMgrViewController : SuperViewController<UITableViewDataSource, UITableViewDelegate, UIScrollViewDelegate, MainSvcDelegate, OrderSvcDelegate, OrderExecuteSvcDelegate, EvalCusDelegate, UIAlertViewDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIButton *         _radio1;
    IBOutlet UIButton *         _radio2;
    IBOutlet UIButton *         _radio3;
    IBOutlet UIButton *         _radio4;
    IBOutlet UIButton *         _radio5;
    
    // news table
    IBOutlet UITableView *      _tableView;
    
    /********************** Member Variable ***********************/
  
    
    NSString *                  mCurLimitTime;
    int                         mCurOrderType;
	
	STOrderInfo *				mSelectedOrderInfo;
}


@property(nonatomic, strong) NSMutableArray*		mOrderArray;
@property(nonatomic, retain) IBOutlet UILabel*		lblNoOrder;


- (void)onClickedPerform:(STOrderInfo *)orderInfo;
- (void)onClickedPerformLongOrder:(STOrderInfo *)orderInfo;
- (void)onClickedPerformInsuranceModal:(STOrderInfo *)orderInfo;
- (IBAction)onClickedRadio1:(id)sender;
- (IBAction)onClickedRadio2:(id)sender;
- (IBAction)onClickedRadio3:(id)sender;
- (IBAction)onClickedRadio4:(id)sender;
- (IBAction)onClickedRadio5:(id)sender;

@end
