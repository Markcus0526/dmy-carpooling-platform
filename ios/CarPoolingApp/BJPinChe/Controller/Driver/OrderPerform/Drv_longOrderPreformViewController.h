//
//  Drv_longOrderPreformViewController.h
//  BJPinChe
//
//  Created by CKK on 14-9-1.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "SuperViewController.h"

@interface Drv_longOrderPreformViewController : SuperViewController <OrderSvcDelegate, LongWayOrderSvcDelegate, UITableViewDataSource, UITableViewDelegate>
{
    /******************************* UI Controls ****************************/
    IBOutlet UILabel *                      _lblDescription;
    IBOutlet UITableView *                  _tblPassenger;
    
    IBOutlet UIButton *                     _btnArrive;
    IBOutlet UIButton *                     _btnStart;
    IBOutlet UIButton *                     _btnFinish;
    
    /******************************* Member Variable *************************/
    STDetailedDrvOrderInfo *                mDetailedOrderInfo;
    BOOL                                    mFirstRun;
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

@property (weak, nonatomic) IBOutlet UIView *progressBarBack;
@property (weak, nonatomic) IBOutlet UIView *progressBarButton;

- (void) SetPassengerUpload : (long)passid password:(NSString *)password;
- (void) callSignLongOrderPassengerGiveup : (long)passid;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedCallCustomer:(id)sender;
- (IBAction)onClickedSetCustomer:(id)sender;
- (IBAction)onClickedChkCustomer:(id)sender;
- (IBAction)onClickedFinish:(id)sender;

@end
