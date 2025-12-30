//
//  Cus_PayOrderViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/15/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface PayCouponCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UIButton *   _chkMark;
@property (nonatomic, strong) IBOutlet UILabel *    _lblName;

- (IBAction)onSelectCheckBox:(id)sender;

@end


@protocol payOrderDelegate<NSObject>
@optional

- (void)onPaySuccess:(long)orderid;

@end


@interface Cus_PayOrderViewController : SuperViewController <UITableViewDataSource, UITableViewDelegate, OrderExecuteSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *          _lblTotalPrice;
    IBOutlet UILabel *          _lblRealPrice;
    IBOutlet UILabel *          _lblCouponPrice;
    
    IBOutlet UITableView *      _tableView;
    
    /********************** Member Variable ***********************/
    NSMutableArray *            mCouponArray;
}

@property (nonatomic, retain) STOrderInfo *		mOrderInfo;
@property (nonatomic, retain) id<payOrderDelegate>		payDelegate;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedPay:(id)sender;

@end

