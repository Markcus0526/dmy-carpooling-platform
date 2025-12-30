//
//  Drv_LongOrderDetailViewController.h
//  BJPinChe
//
//  Created by CKK on 14-9-2.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  长途订单详情

#import "SuperViewController.h"
#import "Drv_EvalCustomerViewController.h"

@interface Drv_LongOrderDetailViewController : SuperViewController <OrderSvcDelegate, LongWayOrderSvcDelegate, UITableViewDataSource, UITableViewDelegate, EvalCusDelegate>
{
    /******************************* UI Controls *************************/
    IBOutlet UITableView *                  _tblPassenger;
    
    /******************************* Member Variable *************************/
    STDetailedDrvOrderInfo *                mDetailedOrderInfo;
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

@property (weak, nonatomic) IBOutlet UILabel *startCityLabel;
@property (weak, nonatomic) IBOutlet UILabel *endCityLabel;
@property (weak, nonatomic) IBOutlet UILabel *orderNumLabel;
@property (weak, nonatomic) IBOutlet UILabel *_lblStartTime;
@property (weak, nonatomic) IBOutlet UILabel *_lblReserveTime;

@property (weak, nonatomic) IBOutlet UILabel *_lblSysDesc;
@property (weak, nonatomic) IBOutlet UILabel *_lblSeatCnt;
@property (weak, nonatomic) IBOutlet UILabel *_lblPrice;
@property (weak, nonatomic) IBOutlet UILabel *_lblState;

@property (weak, nonatomic) IBOutlet UILabel *label1;
@property (weak, nonatomic) IBOutlet UILabel *label2;


@property (weak, nonatomic) IBOutlet UIView *_vwWaitState;
@property (weak, nonatomic) IBOutlet UIView *_vwPerformState;
@property (weak, nonatomic) IBOutlet UIView *_vwExtraState;

- (void)refreshOrderInfo;

- (void)onClickedBtnPassOperate:(STPassengerInfo *)passInfo;
- (void)onClickedBtnPassHeader:(STPassengerInfo *)passInfo;

- (IBAction)OnBackButtonClick:(id)sender;

- (IBAction)onClickedComplain:(id)sender;
- (IBAction)onClickedCancel:(id)sender;
- (IBAction)onClickedPerform:(id)sender;
- (IBAction)onClickedGotoPerform:(id)sender;
- (IBAction)onClickedInsuranceDetail:(id)sender;


@end
