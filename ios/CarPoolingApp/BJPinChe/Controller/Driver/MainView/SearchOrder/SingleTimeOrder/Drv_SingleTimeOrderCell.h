//
//  Drv_SingleTimeOrderCell.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol Drv_SingleDelegate;

@interface Drv_SingleTimeOrderCell : UITableViewCell <OrderSvcDelegate>
{
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblTime;
    IBOutlet UILabel *                  _lblDistance;
    
    __weak IBOutlet UILabel *_lblDIs;
    STSingleTimeOrderInfo *             mOrderInfo;
    
    id                                  mParent;
	
	
	int				mWaitCount;
	
}
- (IBAction)detaileBtn:(id)sender;

- (void) initData : (STSingleTimeOrderInfo *)data parent:(id)parent;
@property (weak, nonatomic) IBOutlet UIButton *publishBtn;
@property (weak, nonatomic) IBOutlet UIImageView *cellBack;

- (IBAction)onClickedPublish:(id)sender;
- (IBAction)onClickedPassenger:(id)sender;
- (IBAction)onClickedBackground:(id)sender;

@property (assign, nonatomic) int indexPathCell;
@property (strong,nonatomic) id<Drv_SingleDelegate>delegate;

@end

@protocol Drv_SingleDelegate <NSObject>
-(void)drv_singleCell:(int)indexPath;

@end
