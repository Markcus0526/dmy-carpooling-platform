//
//  Drv_SearchOrderViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/27/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_SearchOrderViewController : SuperViewController <UITableViewDataSource, UITableViewDelegate, UIScrollViewDelegate, OrderSvcDelegate>
{
    /********************** UI Controls **********************/
    // main control
    IBOutlet UILabel *                  _lblTitle1;
    IBOutlet UILabel *                  _lblTitle2;
    IBOutlet UILabel *                  _ctrlIndicator;
    
    IBOutlet UIScrollView *             _scrollMain;
    IBOutlet UIView *                   _vwSingleTimeOrder;
    IBOutlet UIView *                   _vwWorkTimeOrder;
    
    IBOutlet UITableView *              _tblSingleTimeOrder;
    IBOutlet UITableView *              _tblWorkTimeOrder;
    
    IBOutlet UIButton *                 _radioST1;
    IBOutlet UIButton *                 _radioST2;
    IBOutlet UIButton *                 _radioST3;
    
    /********************** Member Variable ***********************/
    int                                 mCurTab;
    int                                 mSingleCurOrderType;
    long                                mSingleLastOrderID;
    int                                 mSingleCurPageNo;
    int                                 mWorkCurPageNo;
    NSString *                          mWorkCurStartAddr;
    NSString *                          mWorkCurEndAddr;
    long                                 mWorklastOrderID;
    
    NSTimer * 		mWaitAgreeTimer;
    
    STSingleTimeOrderInfo * mOrderInfo ;
    
    int				mWaitCount;
    
    
    NSString * tishiString;
    
   // NSMutableArray *                    mSTOrderArray;
    //NSMutableArray *                    mWTOrderArray;
}
@property(nonatomic,strong)NSMutableArray *mSTOrderArray;
// main control event
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedView1:(id)sender;
- (IBAction)onClickedView2:(id)sender;

// single order control event
- (IBAction)onSetArrange1:(id)sender;
- (IBAction)onSetArrange2:(id)sender;
- (IBAction)onSetArrange3:(id)sender;

-(void)onClickedPerform:(STOrderInfo *)sender;

@end
