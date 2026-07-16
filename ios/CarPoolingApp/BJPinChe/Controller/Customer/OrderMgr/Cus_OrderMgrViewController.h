//
//  NewsMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/23/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//
/**
 *  乘客订单界面
 */
#import <UIKit/UIKit.h>
#import "Cus_MyOrderCell.h"
#import "Cus_EvalDriverViewController.h"
#import "Cus_PayOrderViewController.h"

@interface Cus_OrderMgrViewController : SuperViewController<UITableViewDataSource, UITableViewDelegate, UIScrollViewDelegate, MainSvcDelegate, OrderSvcDelegate, EvalDrvDelegate,LongWayOrderSvcDelegate, payOrderDelegate,OrderExecuteSvcDelegate>
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
  //  NSMutableArray *            mOrderArray;

    NSString *                  mCurLimitTime;
    int                         mCurOrderType;
	
	STOrderInfo *				mTargetOrderInfo;


	IBOutlet UILabel*			lblNoOrder;
}
@property(nonatomic,strong)NSMutableArray *mOrderArray;
- (void)onClickedPerform:(STOrderInfo *)orderInfo;

- (IBAction)onClickedRadio1:(id)sender;
- (IBAction)onClickedRadio2:(id)sender;
- (IBAction)onClickedRadio3:(id)sender;
//- (IBAction)onClickedRadio4:(id)sender;
- (IBAction)onClickedRadio5:(id)sender;

@end
