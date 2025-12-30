//
//  Cus_LongOrderCancelViewController.h
//  BJPinChe
//
//  Created by CKK on 14-9-2.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

/**
 *  长途退票界面 未完成 接口没有
 *
 *  @param IBActionOnBackVeiw:id <#IBActionOnBackVeiw:id description#>
 *
 *  @return <#return value description#>
 */

#import "SuperViewController.h"
#import "Drv_MainTabViewController.h"
@interface Cus_LongOrderCancelViewController : SuperViewController<OrderSvcDelegate, LongWayOrderSvcDelegate>
{
	IBOutlet UILabel *		_lblStartAddr;
	IBOutlet UILabel *		_lblEndAddr;
	IBOutlet UILabel *		_lblOrderNum;
	IBOutlet UILabel * 		_lblPrice;
	IBOutlet UILabel *		_lblPubTime;
	IBOutlet UILabel * 		_lblReserveTime;
	IBOutlet UILabel *		_lblLeftDays;
	IBOutlet UILabel *		_lblReduce;
	IBOutlet UILabel *		_lblRule01;
	IBOutlet UILabel *		_lblRule02;
	IBOutlet UILabel *		_lblRule03;
	IBOutlet UILabel *		_lblRule04;
    
    Drv_MainTabViewController *controller;
}

@property(nonatomic,strong)STDetailedCusOrderInfo *            orderDetailedInfo;

//- (IBAction)OnBackVeiw:(id)sender;
- (IBAction)OnCancelButtonClick:(id)sender;
- (IBAction)OnSureButtonClick:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *time_interval_desc;

@property(nonatomic)long orderID;
@end
